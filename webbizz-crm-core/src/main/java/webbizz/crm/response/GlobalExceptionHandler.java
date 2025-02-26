package webbizz.crm.response;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import webbizz.crm.exception.AbstractApiException;
import webbizz.crm.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * [Exception] 공통 응답 예외 처리 <br />
     * API 호출 후 예외 발생 시 처리하는 핸들러
     *
     * @return {@link ApiErrorResponse} 공통 예외 응답 DTO
     * @see AbstractApiException
     * @author TAEROK HWANG
     */
    @ExceptionHandler(AbstractApiException.class)
    public ResponseEntity<ApiErrorResponse> handleException(final AbstractApiException ex) {
        log.warn("{} [{}] {}", ex.getClass().getSimpleName(), ex.getStatus(), ex.getMessage());

        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.error(ex.getStatus(), ex.getMessage()));
    }

    /**
     * [Exception] Validation 예외 처리 - {@code 400 Bad Request} <br />
     * {@link Validated @Validated} 애노테이션으로 선언된 '객체' 또는 '파라미터' 데이터 값 검증 실패시 발생하는 예외를 처리하는 핸들러
     *
     * @return {@link ApiErrorResponse} 공통 예외 응답 DTO
     * @see MethodArgumentNotValidException
     * @author LEE YONG HO
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleException(final MethodArgumentNotValidException ex,
                                                            final HttpServletRequest request)
            throws MethodArgumentNotValidException {

        log.warn("{} [{}] {}", ex.getClass().getSimpleName(), HttpStatus.BAD_REQUEST, ex.getMessage());
        if (request.getHeader(HttpHeaders.ACCEPT).contains(MediaType.TEXT_HTML_VALUE)) throw ex;

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", ex));
    }

    /**
     * [Exception] JSON 파싱 예외 처리 - {@code 400 Bad Request} <br />
     * JSON 데이터 파싱 실패 시 (특히 Enum) 발생하는 예외를 처리하는 핸들러
     *
     * @return {@link ApiErrorResponse} 공통 예외 응답 DTO
     * @see HttpMessageNotReadableException
     * @author TAEROK HWANG
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleException(final HttpMessageNotReadableException ex,
                                                            final HttpServletRequest request)
            throws HttpMessageNotReadableException {

        log.warn("{} [{}] {}", ex.getClass().getSimpleName(), HttpStatus.BAD_REQUEST, ex.getMessage());
        if (request.getHeader(HttpHeaders.ACCEPT).contains(MediaType.TEXT_HTML_VALUE)) throw ex;

        if (ex.getCause() instanceof InvalidFormatException)
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.",
                            (InvalidFormatException) ex.getCause()));

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."));
    }

    /**
     * [Exception] 예외 처리 - {@code 400 Bad Request} <br />
     * 요청한 파라미터 타입이 잘못된 경우 발생하는 예외를 처리하는 핸들러
     *
     * @return {@link ApiErrorResponse} 공통 예외 응답 DTO
     * @see MethodArgumentTypeMismatchException
     * @author TAEROK HWANG
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleException(final MethodArgumentTypeMismatchException ex,
                                                            final HttpServletRequest request)
            throws MethodArgumentTypeMismatchException {

        log.warn("{} [{}] {}", ex.getClass().getSimpleName(), HttpStatus.BAD_REQUEST, ex.getMessage());
        if (request.getHeader(HttpHeaders.ACCEPT).contains(MediaType.TEXT_HTML_VALUE)) throw ex;

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."));
    }

    /**
     * [Exception] Validation 예외 처리 - {@code 400 Bad Request} <br />
     * 사용자 지정 Validation 실패 시 발생하는 예외를 처리하는 핸들러
     *
     * @return {@link ApiErrorResponse} 공통 예외 응답 DTO
     * @see ConstraintViolationException
     * @author TAEROK HWANG
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleException(final ConstraintViolationException ex,
                                                            final HttpServletRequest request)
            throws ConstraintViolationException {

        log.warn("{} [{}] {}", ex.getClass().getSimpleName(), HttpStatus.BAD_REQUEST, ex.getMessage());
        if (request.getHeader(HttpHeaders.ACCEPT).contains(MediaType.TEXT_HTML_VALUE)) throw ex;

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", ex));
    }

    /**
     * [Exception] 예외 처리 - {@code 403 Forbidden} <br />
     * 접근 권한이 없는 경우 발생하는 예외를 처리하는 핸들러
     *
     * @return {@link ApiErrorResponse} 공통 예외 응답 DTO
     * @see AccessDeniedException
     * @author TAEROK HWANG
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleException(final AccessDeniedException ex,
                                                            final HttpServletRequest request)
            throws AccessDeniedException {

        log.warn("{} [{}] {}", ex.getClass().getSimpleName(), HttpStatus.FORBIDDEN, request.getRequestURI());
        if (request.getHeader(HttpHeaders.ACCEPT).contains(MediaType.TEXT_HTML_VALUE)) throw ex;

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."));
    }

//    /**
//     * [Exception] 예외 처리 - {@code 404 Not Found} <br />
//     * 요청한 페이지를 찾을 수 없는 경우 발생하는 예외를 처리하는 핸들러 (Spring Framework 6 부터 사용 가능)
//     *
//     * @return {@link ApiErrorResponse} 공통 예외 응답 DTO
//     * @see NoResourceFoundException
//     * @author TAEROK HWANG
//     */
//    @ExceptionHandler(NoResourceFoundException.class)
//    public ResponseEntity<ApiErrorResponse> handleException(final NoResourceFoundException ex,
//                                                            final HttpServletRequest request)
//            throws NoResourceFoundException {
//
//        log.warn("{} [{}] {}", ex.getClass().getSimpleName(), HttpStatus.NOT_FOUND, ex.getResourcePath());
//        if (request.getHeader(HttpHeaders.ACCEPT).contains(MediaType.TEXT_HTML_VALUE)) throw ex;
//
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(ApiResponse.error(HttpStatus.NOT_FOUND, "요청한 페이지를 찾을 수 없습니다."));
//    }

    /**
     * [Exception] 예외 처리 - {@code 405 Method Not Allowed} <br />
     * 지원하지 않는 요청 메소드인 경우 발생하는 예외를 처리하는 핸들러
     *
     * @return {@link ApiErrorResponse} 공통 예외 응답 DTO
     * @see HttpRequestMethodNotSupportedException
     * @author TAEROK HWANG
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiErrorResponse> handleException(final HttpRequestMethodNotSupportedException ex,
                                                            final HttpServletRequest request)
            throws HttpRequestMethodNotSupportedException {

        log.warn("{} [{}] {} {}",
                ex.getClass().getSimpleName(), HttpStatus.METHOD_NOT_ALLOWED, ex.getMethod(), request.getRequestURI());
        if (request.getHeader(HttpHeaders.ACCEPT).contains(MediaType.TEXT_HTML_VALUE)) throw ex;

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error(
                        HttpStatus.METHOD_NOT_ALLOWED,
                        String.format("지원하지 않는 요청입니다: %s %s", ex.getMethod(), request.getRequestURI())));
    }

    /**
     * [Exception] 예외 처리 - {@code 415 Unsupported Media Type} <br />
     * 지원하지 않는 콘텐츠 유형인 경우 발생하는 예외를 처리하는 핸들러
     *
     * @return {@link ApiErrorResponse} 공통 예외 응답 DTO
     * @see HttpMediaTypeNotSupportedException
     * @author TAEROK HWANG
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleException(final HttpMediaTypeNotSupportedException ex)
            throws HttpMediaTypeNotSupportedException {

        log.warn("{} [{}] {}", ex.getClass().getSimpleName(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getContentType());
        if (ex.getSupportedMediaTypes().contains(MediaType.TEXT_HTML)) throw ex;

        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(ApiResponse.error(
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                        "지원하지 않는 콘텐츠 유형입니다: " + ex.getContentType()));
    }

    /**
     * [Exception] 수동 예외 처리
     *
     * @see BusinessException
     * @author TAEROK HWANG
     */
    @ExceptionHandler(BusinessException.class)
    public void handleException(final BusinessException ex, final HttpServletResponse response) throws IOException {
        log.warn("{} [{}] {}", ex.getClass().getSimpleName(), ex.getStatus(), ex.getMessage());

        response.sendError(ex.getStatus().value(), ex.getMessage());
    }

}
