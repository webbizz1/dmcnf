package webbizz.crm.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Duration;

@ConfigurationProperties(prefix = "webbizz.jwt")
@Validated
@Getter
public class JwtProperties {

    /**
     * JWT 서명 키
     */
    @Size(min = 32)
    private final String secretKey;

    /**
     * JWT 만료 시간
     */
    private final Duration expirationTime;

    /**
     * JWT 서명 알고리즘
     */
    @Pattern(regexp = "^(HS256|HS384|HS512)$", message = "지원하지 않는 JWT 서명 알고리즘입니다.")
    private final String algorithm;

    @ConstructorBinding
    public JwtProperties(@DefaultValue("webbizzcrmapplicationjwtsecret!@") String secretKey,
                         @DefaultValue("10m") Duration expirationTime,
                         @DefaultValue("HS256") String algorithm) {

        this.secretKey = secretKey;
        this.expirationTime = expirationTime;

        if ("HS384".equals(algorithm) && secretKey.length() < 48) {
            throw new IllegalArgumentException("HS384 알고리즘은 48자 이상의 비밀키가 필요합니다.");
        }
        if ("HS512".equals(algorithm) && secretKey.length() < 64) {
            throw new IllegalArgumentException("HS384 알고리즘은 64자 이상의 비밀키가 필요합니다.");
        }

        this.algorithm = algorithm;
    }

}
