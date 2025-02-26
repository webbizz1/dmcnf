package webbizz.crm.web.pages;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import webbizz.crm.domain.boardarticle.dto.BoardArticleCondition;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.domain.menu.dto.MenuPageDto;
import webbizz.crm.domain.menu.enumset.MenuMode;
import webbizz.crm.exception.BusinessException;
import webbizz.crm.response.ApiResponse;
import webbizz.crm.service.menu.MenuRenderService;
import webbizz.crm.service.menu.MenuService;

@Controller
@RequiredArgsConstructor
public class MenuPageController {

    private final MenuService menuService;
    private final MenuRenderService menuRenderService;

    /**
     * 메뉴 페이지 조회
     *
     * @param menuId 메뉴 시퀀스
     * @param model  Model
     * @return 메뉴 관리에서 정의한 콘텐츠 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/pages/{menuId}")
    public String menuPage(@PathVariable(name = "menuId") Long menuId,
                           @RequestParam(value = "menuMode", defaultValue = "LIST", required = false) MenuMode menuMode,
                           @ModelAttribute("boardCondition") BoardArticleCondition boardCondition,
                           @AuthenticationPrincipal MemberUserDetails memberUserDetails,
                           Model model) {

        MenuPageDto menuPageDto = menuService.getMenuPage(menuId);
        model.addAttribute("menuPage", menuPageDto);

        switch (menuPageDto.getMenu().getType()) {
            case LINK: {
                if (StringUtils.hasText(menuPageDto.getMenu().getLinkUrl()))
                    return "redirect:" + menuPageDto.getMenu().getLinkUrl();

                return "index";
            }

            case CONTENT: {
                return "pages/type_content";
            }

            case BOARD: {
                ModelAndView modelAndView =
                        menuRenderService.renderBoardPage(menuPageDto, menuMode, boardCondition, memberUserDetails);

                model.addAllAttributes(modelAndView.getModel());
                return modelAndView.getViewName();
            }

            default:
                throw new BusinessException(404, "페이지를 찾을 수 없습니다.");
        }
    }

    @GetMapping("/pdf-viewer")
    public String pdfViewer() {
        return "pages/pdf_viewer";
    }


    @GetMapping("/api/v1/internal/menu/evict-cache")
    @ResponseBody
    public ApiResponse<Void> menuEvictCache(@RequestParam(value = "groupId", defaultValue = "1") Long groupId) {
        menuService.evictMenuCache(groupId);
        return ApiResponse.ok();
    }

}
