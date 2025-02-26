package webbizz.crm.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    /**
     * 로그인
     *
     * @param session HttpSession
     * @param model Model
     * @return 로그인 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        String errorMessage = (String) session.getAttribute("errorMessage");

        // 로그인 실패 오류 메시지
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        return "login";
    }

    @GetMapping("/change-password")
    public String resetPassword() {
        return "change_password";
    }

}
