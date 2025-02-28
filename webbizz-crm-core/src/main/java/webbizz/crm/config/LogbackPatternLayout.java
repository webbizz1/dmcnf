package webbizz.crm.config;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class LogbackPatternLayout extends PatternLayout {

    static {
        PatternLayout.DEFAULT_CONVERTER_MAP.put("USER", UserConverter.class.getName());
        PatternLayout.DEFAULT_CONVERTER_MAP.put("SESSION", SessionConverter.class.getName());
    }

    /**
     * logback 에서 사용자 정보를 출력하기 위한 변수를 convert 해주는 Class<br />
     * <code>logback-spring.xml</code> 에서 <code>%SESSION</code> 으로 값 사용
     *
     * @since 2023-05-03
     * @author LimHunMuk
     */
    public static class UserConverter extends ClassicConverter {
        @Override
        public String convert(ILoggingEvent event) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null)
                return "anonymousUser";

            return authentication.getName();
        }
    }

    /**
     * logback 에서 세션 정보를 출력하기 위한 변수를 convert 해주는 Class<br />
     * <code>logback-spring.xml</code> 에서 <code>%USER</code> 으로 값 사용
     *
     * @since 2023-05-03
     * @author LimHunMuk
     */
    public static class SessionConverter extends ClassicConverter {
        @Override
        public String convert(ILoggingEvent event) {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            if (attributes == null)
                return "";

            return attributes.getSessionId();
        }
    }

}
