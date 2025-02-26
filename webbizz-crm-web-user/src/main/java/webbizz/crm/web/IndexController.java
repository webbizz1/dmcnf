package webbizz.crm.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import webbizz.crm.domain.boardarticle.dto.BoardArticleCondition;
import webbizz.crm.domain.boardarticle.dto.BoardArticleListDto;
import webbizz.crm.domain.exhibition.dto.ExhibitionDto;
import webbizz.crm.domain.exhibition.enumset.ExhibitionType;
import webbizz.crm.service.board.BoardArticleService;
import webbizz.crm.service.exhibition.ExhibitionService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final BoardArticleService boardArticleService;
    private final ExhibitionService exhibitionService;

    @GetMapping("/")
    public String index(Model model) {
        BoardArticleCondition condition = new BoardArticleCondition();

        //배너
        List<ExhibitionDto> exhibitionList = exhibitionService.searchAllForActive();

        //메인
        List<ExhibitionDto> mainExhibitionList = exhibitionList.stream()
                .filter(exhibition -> exhibition.getType().equals(ExhibitionType.MAIN))
                .collect(Collectors.toList());
        model.addAttribute("main", mainExhibitionList);

        //홍보배너_1
        List<ExhibitionDto> promoExhibitionList = exhibitionList.stream()
                .filter(exhibition -> exhibition.getType().equals(ExhibitionType.PROMOTION))
                .collect(Collectors.toList());
        model.addAttribute("promotion1", promoExhibitionList);

        //홍보배너_2
        List<ExhibitionDto> promoExhibitionList2 = exhibitionList.stream()
                .filter(exhibition -> exhibition.getType().equals(ExhibitionType.PROMOTION_2))
                .collect(Collectors.toList());
        model.addAttribute("promotion2", promoExhibitionList2);

        //메인_알림
        condition.setBoardType("notice");
        condition.setSize(8);
        List<BoardArticleListDto> notice = boardArticleService.searchAllByBoardType(condition);
        model.addAttribute("notice", notice);

        //메인_뉴스
        condition.setBoardType("news");
        condition.setSize(4);
        List<BoardArticleListDto> news = boardArticleService.searchAllByBoardType(condition);
        model.addAttribute("news", news);
        return "index";
    }

}
