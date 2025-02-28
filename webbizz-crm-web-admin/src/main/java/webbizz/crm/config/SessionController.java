package webbizz.crm.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SessionController {


    private final HttpSession session;

    @PostMapping("/session-time-left")
    public long getSessionTimeLeft(HttpSession session) {
        long creationTime = session.getCreationTime(); // 세션이 생성된 시간 (밀리초)
        int maxInactiveInterval = session.getMaxInactiveInterval(); // 세션 유지 시간 (초)
        long now = System.currentTimeMillis(); // 현재 시간 (밀리초)

        long expiryTime = creationTime + (maxInactiveInterval * 1000L); // 세션 만료 예정 시간
        long timeLeft = expiryTime - now; // 남은 시간 계산

        if (timeLeft < 0) {
            return 0; // 세션 만료
        }
        return timeLeft / 1000; // 초 단위 반환
    }

    @After("execution(@org.springframework.web.bind.annotation.GetMapping * *(..))") // @GetMapping이 적용된 메서드 실행 전
    public void resetSession() {
        if (session != null) {
            session.setMaxInactiveInterval(3600); // 세션 유지 시간 60분 (3600초)으로 설정
            log.info("세션");
        }
    }
}
