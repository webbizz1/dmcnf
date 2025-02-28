package webbizz.crm.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import webbizz.crm.util.PatternUtils;
import webbizz.crm.util.RequestUtils;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * API 표준 오류 응답 객체
 *
 * @author TAEROK HWANG
 */
@Getter
public class ApiErrorResponse {

    /**
     * 요청 시간
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternUtils.DATE_TIME_MILLS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime timestamp;

    /**
     * HTTP 상태 코드
     */
    private final HttpStatus status;

    /**
     * HTTP 상태 문자열
     */
    private final String error;

    /**
     * status=200: OK, 이외: 오류 메시지
     */
    private final String message;

    /**
     * 요청 경로
     */
    private final String path;

    /**
     * Validation 실패 시 오류 내용 리스트
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<ApiFieldError> errors;

    private ApiErrorResponse(HttpStatus status, String message, List<ApiFieldError> errors) {

        this.timestamp = LocalDateTime.now();

        if (status == null)
            throw new IllegalArgumentException("status must not be null");

        if (!status.is4xxClientError() && !status.is5xxServerError())
            throw new IllegalArgumentException("status must be 4xx or 5xx");

        this.status = status;
        this.error = status.getReasonPhrase();

        if (!StringUtils.hasText(message))
            throw new IllegalArgumentException("message must not be empty");

        this.message = message;
        this.path = RequestUtils.getPath();
        this.errors = errors;
    }

    public ApiErrorResponse(HttpStatus status, String message) {
        this(status, message, (List<ApiFieldError>) null);
    }

    public ApiErrorResponse(HttpStatus status, String message, MethodArgumentNotValidException ex) {
        this(status, message, ApiFieldError.createFieldErrors(ex));
    }

    public ApiErrorResponse(HttpStatus status, String message, InvalidFormatException ex) {
        this(status, message, ApiFieldError.createFieldErrors(ex));
    }

    public ApiErrorResponse(HttpStatus status, String message, ConstraintViolationException ex) {
        this(status, message, ApiFieldError.createFieldErrors(ex));
    }

    @JsonGetter("status")
    public Integer getStatusValue() {
        return this.status.value();
    }

    @AllArgsConstructor
    @Getter
    public static class ApiFieldError {

        private final String message;

        private final String field;

        private final Object rejectedValue;

        public static List<ApiFieldError> createFieldErrors(MethodArgumentNotValidException ex) {
            if (ex == null) return null;

            return ex.getBindingResult().getFieldErrors().stream()
                    .map(error -> new ApiFieldError(
                            error.getDefaultMessage(),
                            error.getField(),
                            error.getRejectedValue()))
                    .collect(Collectors.toList());
        }

        private static final Pattern ENUM_MESSAGE_ACCEPTED =
                Pattern.compile("not one of the values accepted for Enum class: (\\[.*])");
        private static final Pattern ENUM_MESSAGE_EMPTY =
                Pattern.compile("Cannot coerce empty String");

        public static List<ApiFieldError> createFieldErrors(InvalidFormatException ex) {
            if (ex == null) return null;

            Matcher matcherAccepted = ENUM_MESSAGE_ACCEPTED.matcher(ex.getMessage());
            Matcher matcherEmpty = ENUM_MESSAGE_EMPTY.matcher(ex.getMessage());
            final String message = matcherAccepted.find()
                    ? "다음 값 중 하나여야 합니다: " + matcherAccepted.group(1).replace("NONE, ", "")
                    : (matcherEmpty.find() ? "값이 비어있을 수 없습니다." : ex.getMessage());

            return ex.getPath().stream()
                    .map(path -> new ApiFieldError(message, path.getFieldName(), ex.getValue()))
                    .collect(Collectors.toList());
        }

        public static List<ApiFieldError> createFieldErrors(ConstraintViolationException ex) {
            if (ex == null) return null;

            return ex.getConstraintViolations().stream()
                    .map(violation -> new ApiFieldError(
                            violation.getMessage(),
                            violation.getPropertyPath().toString(),
                            violation.getInvalidValue()))
                    .collect(Collectors.toList());
        }

    }

}
