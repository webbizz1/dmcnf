package webbizz.crm.web.legal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import webbizz.crm.domain.terms.enumset.TermsType;
import webbizz.crm.service.terms.TermsService;

@Controller
@RequiredArgsConstructor
public class TermsController {

    private final TermsService termsService;

    /**
     * 이용약관
     *
     * @return 이용약관 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("terms", termsService.searchByForActive(TermsType.TERMS));
        return "legal/terms";
    }

    /**
     * 개인정보처리방침
     *
     * @return 개인정보처리방침 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("terms", termsService.searchByForActive(TermsType.PRIVACY));
        return "legal/terms";
    }

    /**
     * 사이트맵
     *
     * @return 사이트맵 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/sitemap")
    public String sitemap() {
        return "legal/sitemap";
    }


}
