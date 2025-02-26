package webbizz.crm.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import webbizz.crm.util.PatternUtils;
import webbizz.crm.util.RequestUtils;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

/**
 * API 표준 응답 객체
 *
 * @param <T> 응답 데이터 타입
 * @author TAEROK HWANG
 */
@Getter
public class ApiResponse<T> {

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
     * status=200: OK, 이외: 오류 메시지
     */
    private final String message;

    /**
     * 요청 경로
     */
    private final String path;

    /**
     * 응답 데이터
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    private ApiResponse(HttpStatus status, String message, String path, T data) {
        this.timestamp = LocalDateTime.now();

        if (status == null) {
            this.status = HttpStatus.OK;
        } else {
            if (!status.is2xxSuccessful()) {
                throw new IllegalArgumentException("status must be 2xx");
            }
            this.status = status;
        }

        this.message = StringUtils.hasText(message) ? message : this.status.getReasonPhrase();
        this.path = (path != null) ? path : RequestUtils.getPath();
        this.data = data;
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(HttpStatus.OK, null, null, null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(HttpStatus.OK, null, null, data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK, message, null, data);
    }

    public static ApiErrorResponse error(HttpStatus status, String message) {
        return new ApiErrorResponse(status, message);
    }

    public static ApiErrorResponse error(HttpStatus status, String message, MethodArgumentNotValidException ex) {
        return new ApiErrorResponse(status, message, ex);
    }

    public static ApiErrorResponse error(HttpStatus status, String message, InvalidFormatException ex) {
        return new ApiErrorResponse(status, message, ex);
    }

    public static ApiErrorResponse error(HttpStatus status, String message, ConstraintViolationException ex) {
        return new ApiErrorResponse(status, message, ex);
    }

    @JsonGetter("status")
    public Integer statusValue() {
        return this.status.value();
    }

}
