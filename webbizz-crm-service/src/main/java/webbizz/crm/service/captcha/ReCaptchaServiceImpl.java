package webbizz.crm.service.captcha;

import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import webbizz.crm.domain.boardarticle.dto.ReCaptchaResponseDto;
import webbizz.crm.exception.AbstractApiException;
import webbizz.crm.exception.ApiBadRequestException;

@Service("captchaService")
@RequiredArgsConstructor
public class ReCaptchaServiceImpl extends EgovAbstractServiceImpl implements CaptchaService {

    @Value("${external.google.recaptcha.secret-key:}")
    private String secretKey;

    @Value("${external.google.recaptcha.verify-url:https://www.google.com/recaptcha/api/siteverify}")
    private String verifyUrl;

    private final RestTemplate restTemplate;

    @Override
    public boolean validate(final String token) throws AbstractApiException {
        // reCAPTCHA 인증 처리 (application/x-www-form-urlencoded 만 가능)
        LinkedMultiValueMap<Object, Object> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", this.secretKey);
        requestMap.add("response", token);

        ResponseEntity<ReCaptchaResponseDto> response =
                restTemplate.postForEntity(this.verifyUrl, new HttpEntity<>(requestMap), ReCaptchaResponseDto.class);

        ReCaptchaResponseDto responseBody = response.getBody();

        if (response.getStatusCode() != HttpStatus.OK || responseBody == null) {
            throw new ApiBadRequestException("CAPTCHA 인증 응답을 받지 못했습니다.");
        }
        if (!responseBody.isSuccess()) {
            String errorCode = responseBody.getErrorCodes().stream().findFirst().orElse("");
            String errorMessage = "";

            switch (errorCode) {
                case "missing-input-secret":
                    errorMessage = "보안 비밀 매개변수가 누락되었습니다.";
                    break;

                case "invalid-input-secret":
                    errorMessage = "보안 비밀 매개변수가 올바르지 않거나 형식이 잘못되었습니다.";
                    break;

                case "missing-input-response":
                    errorMessage = "응답 매개변수가 누락되었습니다.";
                    break;

                case "invalid-input-response":
                    errorMessage = "응답 매개변수가 유효하지 않거나 형식이 잘못되었습니다.";
                    break;

                case "bad-request":
                    errorMessage = "요청이 잘못되었거나 형식이 잘못되었습니다.";
                    break;

                case "timeout-or-duplicate":
                    errorMessage = "응답이 만료되었거나 중복되었습니다.";
                    break;
            }

            throw new ApiBadRequestException(String.format("CAPTCHA 인증에 실패했습니다. (%s)", errorMessage));
        }

        return true;
    }

}
