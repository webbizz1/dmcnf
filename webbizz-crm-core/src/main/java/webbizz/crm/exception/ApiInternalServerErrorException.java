package webbizz.crm.exception;

import org.springframework.http.HttpStatus;

public class ApiInternalServerErrorException extends AbstractApiException {

    public ApiInternalServerErrorException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public ApiInternalServerErrorException(int statusCode, String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, statusCode, message);
    }

}
