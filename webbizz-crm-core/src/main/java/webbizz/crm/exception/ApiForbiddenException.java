package webbizz.crm.exception;

import org.springframework.http.HttpStatus;

public class ApiForbiddenException extends AbstractApiException {

    public ApiForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

    public ApiForbiddenException(int statusCode, String message) {
        super(HttpStatus.FORBIDDEN, statusCode, message);
    }

}
