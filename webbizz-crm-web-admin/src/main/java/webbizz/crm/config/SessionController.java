package webbizz.crm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
public class SessionController {

    @GetMapping("/session-time-left")
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
}
