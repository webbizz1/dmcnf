package webbizz.crm.web.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webbizz.crm.domain.search.dto.SearchCondition;
import webbizz.crm.domain.search.dto.SearchDto;
import webbizz.crm.service.menu.MenuService;
import webbizz.crm.service.search.SearchService;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final MenuService menuService;
    private final SearchService searchService;


    /**
     * 메뉴 검색 조회
     *
     * @param text 검색어
     * @return 검색결과
     * @author koyeji
     */
    @GetMapping("/search")
    public String menuPage(@ModelAttribute("text") String text, Model model) {
        SearchDto searchDto = searchService.searchBySearchText(text);
        searchDto.getMenuDtoList().forEach((m) -> {
            m.setMenuPageDto(menuService.getMenuPage(m.getId()));
        });
        model.addAttribute("searchDto",searchDto);
        return "pages/type_search_result";
    }

    /**
     * 메뉴 검색 조회
     *
     * @param condition 검색조건
     * @param model model
     * @return 검색결과 페이지
     * @author koyeji
     */
    @GetMapping("/search/page")
    public String menuPage(@ModelAttribute("condition") SearchCondition condition, Model model) {
        condition.setSize(30);
        model.addAttribute("menuPageDto",menuService.getMenuPage(condition.getMenuId()));
        model.addAttribute("pageVars", searchService.searchBoardByCondition(condition));
        return "pages/type_search_detail";
    }
}


