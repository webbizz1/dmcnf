package webbizz.crm.exception;

import org.springframework.http.HttpStatus;

public class ApiUnauthorizedException extends AbstractApiException {

    public ApiUnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public ApiUnauthorizedException(int statusCode, String message) {
        super(HttpStatus.UNAUTHORIZED, statusCode, message);
    }

}
