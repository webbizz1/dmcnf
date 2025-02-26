package webbizz.crm.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import webbizz.crm.config.properties.JwtProperties;
import webbizz.crm.exception.AbstractApiException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 유틸리티 클래스 <br />
 * io.jsonwebtoken 라이브러리 사용 <br />
 * com.mimbusds.jose 에서 io.jsonwebtoken 라이브러리로 변경
 *
 * @since 2024-06-18
 * @see JwtProperties
 * @author TAEROK HWANG
 * @version 1.2
 */
@Slf4j
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class JwtUtils {

    private final JwtProperties properties;

    private final SecretKey hmacShaKey;

    private final SecureDigestAlgorithm<? super SecretKey, ?> secureDigestAlgorithm;

    /** 토큰 발급자 (issuer) */
    public static final String PAYLOAD_ISSUER = "iss";
    /** 토큰 제목 (subject) */
    public static final String PAYLOAD_SUBJECT = "sub";
    /** 토큰 대상자 (audience) */
    public static final String PAYLOAD_AUDIENCE = "aud";
    /** 토큰 만료 시간 (expiration time) */
    public static final String PAYLOAD_EXPIRATION_TIME = "exp";
    /** 토큰 활성 시간 (not before) */
    public static final String PAYLOAD_NOT_BEFORE = "nbf";
    /** 토큰 발급 시간 (issued at) */
    public static final String PAYLOAD_ISSUED_AT = "iat";
    /** 토큰 고유 식별자 (JWT ID) */
    public static final String PAYLOAD_JWT_ID = "jti";

    @SuppressWarnings("unchecked")
    public JwtUtils(JwtProperties properties) {
        this.properties = properties;
        this.hmacShaKey = Keys.hmacShaKeyFor(properties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        this.secureDigestAlgorithm =
                (SecureDigestAlgorithm<? super SecretKey, ?>) Jwts.SIG.get().get(properties.getAlgorithm());
    }

    public TokenBuilder builder() {
        return new TokenBuilder();
    }

    public TokenParser parser(String token) {
        return new TokenParser(token);
    }

    /**
     * 토큰 생성<br />
     * 토큰 만료 시간을 설정하지 않으면 기본 값은 jwt.expiration-time 값을 읽는다. (기본 값: 10분)
     *
     * @see #createToken(Map, Long)
     * @param payload 토큰에 담을 정보 (body)
     * @return String JSON Web Token
     * @author TAEROK HWANG
     */
    public String createToken(Map<String, Object> payload) {
        return this.createToken(payload, properties.getExpirationTime().getSeconds());
    }

    /**
     * 토큰 생성
     *
     * @param payload 토큰에 담을 정보 (body)
     * @param expirationSeconds 토큰 만료 시간 (단위: 초)
     * @return String JSON Web Token
     * @author TAEROK HWANG
     */
    public String createToken(Map<String, Object> payload, Long expirationSeconds) {
        Date now = new Date();

        // JWT 문자열 빌드 및 서명
        try {
            return Jwts.builder()
                    .header()
                    .type("JWT")
                    .and()
                    .issuedAt(now)
                    .expiration(new Date(now.getTime() + expirationSeconds * 1000))
//                    .subject(getClass().getPackageName())
                    .claims(payload)
                    .signWith(this.hmacShaKey, this.secureDigestAlgorithm)
                    .compact();
        } catch (IllegalArgumentException e) {
            log.error("JWT Payload 데이터 오류: ", e);
            return null;
        } catch (WeakKeyException e) {
            log.error("JWT 서명 암호키 길이 부족: ", e);
            return null;
        }
    }

    /**
     * JWT 토큰 빌더 클래스
     */
    public class TokenBuilder {

        private final Map<String, Object> payload = new HashMap<>();
        private Long expirationSeconds;

        /**
         * 만료 시간 설정
         *
         * @param expirationSeconds 만료 시간 (단위: 초)
         * @return TokenBuilder (Method Chaining)
         * @see #expirationTime(Duration)
         * @author TAEROK HWANG
         */
        public TokenBuilder expirationTime(Long expirationSeconds) {
            this.expirationSeconds = expirationSeconds;
            return this;
        }

        /**
         * 만료 시간 설정
         *
         * @param duration 만료 시간
         * @return TokenBuilder (Method Chaining)
         * @see #expirationTime(Long)
         * @author TAEROK HWANG
         */
        public TokenBuilder expirationTime(Duration duration) {
            this.expirationSeconds = duration.getSeconds();
            return this;
        }

        /**
         * 토큰에 담을 정보 추가
         *
         * @param key 키 이름
         * @param value 키 값
         * @return TokenBuilder (Method Chaining)
         * @author TAEROK HWANG
         */
        public TokenBuilder claim(String key, Object value) {
            payload.put(key, value);
            return this;
        }

        /**
         * 토큰 생성
         *
         * @return String JSON Web Token
         * @author TAEROK HWANG
         */
        public String build() {
            if (expirationSeconds != null)
                return createToken(payload, expirationSeconds);
            else
                return createToken(payload);
        }

    }

    /**
     * JWT 토큰 파서 클래스
     */
    public class TokenParser {

        private final String token;

        /**
         * JWT 토큰 파싱에 필요한 생성자
         * @param token JSON Web Token
         * @author TAEROK HWANG
         */
        public TokenParser(String token) {
            if (!StringUtils.hasText(token))
                throw new JwtParseException("토큰이 존재하지 않습니다.");

            this.token = token;
        }

        /**
         * JWT 토큰 파서 설정
         *
         * @return TokenClaims 토큰 정보가 담긴 TokenClaims 객체
         * @author TAEROK HWANG
         */
        public TokenClaims verify() throws JwtParseException, JwtSignatureException, JwtExpiredException {
            JwtParser parser = Jwts.parser()
                    .verifyWith(hmacShaKey)
                    .build();

            return this.parse(parser, token);
        }

        /**
         * JWT 토큰 파서 설정 (만료 시간 검증 없음)
         *
         * @return TokenClaims 토큰 정보가 담긴 TokenClaims 객체
         * @author TAEROK HWANG
         */
        public TokenClaims verifyWithoutExpiration() throws JwtParseException, JwtSignatureException {
            JwtParser parser = Jwts.parser()
                    .verifyWith(hmacShaKey)
                    .clockSkewSeconds(Integer.MAX_VALUE) // 만료 시간 검증을 하지 않음
                    .build();

            return this.parse(parser, token);
        }

        /**
         * JWT 토큰 파싱
         *
         * @param parser JWT 파서 객체
         * @param token JSON Web Token
         * @return TokenClaims 토큰 정보가 담긴 TokenClaims 객체
         * @throws JwtParseException 토큰 파싱에 실패했을 때
         * @throws JwtSignatureException 토큰 서명 검증에 실패했을 때
         * @throws JwtExpiredException 토큰이 만료되었을 때
         * @author TAEROK HWANG
         */
        private TokenClaims parse(JwtParser parser, String token)
                throws JwtParseException, JwtSignatureException, JwtExpiredException {

            if (!StringUtils.hasText(token))
                throw new JwtParseException("토큰 정보가 없습니다.");

            try {
                return new TokenClaims(parser.parse(token));
            } catch (MalformedJwtException e) {
                log.warn("JWT 토큰 파싱 실패: {}, token={}", e.getMessage(), token);
                throw new JwtParseException("토큰 파싱에 실패했습니다.");
            } catch (SignatureException e) {
                log.warn("JWT 토큰 서명 검증 실패: {}", e.getMessage());
                throw new JwtSignatureException("토큰 서명 검증에 실패했습니다.");
            } catch (ExpiredJwtException e) {
                log.warn("JWT 토큰 만료: {}", e.getMessage());
                throw new JwtExpiredException("토큰이 만료되었습니다.");
            }
        }
    }

    /**
     * JWT 토큰 Payload 정보를 담는 클래스
     */
    @Getter
    public static class TokenClaims {

        private final Claims payload;

        private TokenClaims(Jwt<?, ?> jwt) {
            this.payload = (Claims) jwt.getPayload();
        }

    }

    public static abstract class JwtException extends AbstractApiException {
        public JwtException(HttpStatus status, String message) {
            super(status, message);
        }
    }

    public static class JwtParseException extends JwtException {
        public JwtParseException(String message) {
            super(HttpStatus.BAD_REQUEST, message);
        }
    }

    public static class JwtSignatureException extends JwtException {
        public JwtSignatureException(String message) {
            super(HttpStatus.UNAUTHORIZED, message);
        }
    }

    public static class JwtExpiredException extends JwtException {
        public JwtExpiredException(String message) {
            super(HttpStatus.UNAUTHORIZED, message);
        }
    }

}
