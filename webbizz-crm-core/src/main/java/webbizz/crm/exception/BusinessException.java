package webbizz.crm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(int statusCode, String message) {
        super(message);
        this.status = HttpStatus.valueOf(statusCode);
    }

}
