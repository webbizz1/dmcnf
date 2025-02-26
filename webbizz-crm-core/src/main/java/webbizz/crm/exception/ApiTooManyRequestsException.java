package webbizz.crm.exception;

import org.springframework.http.HttpStatus;

public class ApiTooManyRequestsException extends AbstractApiException {

    public ApiTooManyRequestsException(String message) {
        super(HttpStatus.TOO_MANY_REQUESTS, message);
    }

    public ApiTooManyRequestsException(int statusCode, String message) {
        super(HttpStatus.TOO_MANY_REQUESTS, statusCode, message);
    }

}
