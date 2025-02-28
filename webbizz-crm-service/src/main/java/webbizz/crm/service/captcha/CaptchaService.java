package webbizz.crm.service.captcha;

import webbizz.crm.exception.AbstractApiException;

public interface CaptchaService {

    /**
     * CAPTCHA 인증 처리
     *
     * @param token CAPTCHA 인증 토큰
     * @return 인증 성공 여부
     * @author TAEROK HWANG
     */
    @SuppressWarnings("UnusedReturnValue")
    boolean validate(String token) throws AbstractApiException;

}
