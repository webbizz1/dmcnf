package webbizz.crm.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.service.IndexService;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final IndexService indexService;

    /**
     * 인덱스 페이지
     *
     * @return 인덱스 페이지 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/")
    public String index(@AuthenticationPrincipal MemberUserDetails memberUserDetails, Model model) {
        ModelAndView modelAndView = indexService.getIndexCounts(memberUserDetails);
        model.addAllAttributes(modelAndView.getModel());

        return "index";
    }

}
