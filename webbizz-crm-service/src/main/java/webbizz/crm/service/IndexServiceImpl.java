package webbizz.crm.service;

import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import webbizz.crm.domain.exhibition.enumset.ExhibitionType;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.domain.statistics.dto.StatisticsCondition;
import webbizz.crm.service.board.BoardArticleService;
import webbizz.crm.service.exhibition.ExhibitionService;
import webbizz.crm.service.member.MemberService;
import webbizz.crm.service.statistics.StatisticsService;
import webbizz.crm.service.survey.SurveyService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service("indexService")
@RequiredArgsConstructor
public class IndexServiceImpl extends EgovAbstractServiceImpl implements IndexService {

    private final BoardArticleService boardArticleService;
    private final ExhibitionService exhibitionService;
    private final MemberService memberService;
    private final SurveyService surveyService;
    private final StatisticsService statisticsService;

    /**
     * 인덱스 페이지 카운트 조회
     *
     * @param memberUserDetails 사용자 정보
     * @author TAEROK HWANG
     */
    public ModelAndView getIndexCounts(final MemberUserDetails memberUserDetails) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("memberCount", memberService.countMemberForActive());
        modelAndView.addObject("surveyCount", surveyService.countSurveyForActive());
        modelAndView.addObject("exhibitionCount", exhibitionService.countExhibitionForActive(null));
        modelAndView.addObject("exhibitionPopupCount",
                exhibitionService.countExhibitionForActive(ExhibitionType.POPUP));

        modelAndView.addObject("recentNotice", boardArticleService.searchRecentNotice());
        modelAndView.addObject("recentMyArticles",
                boardArticleService.searchRecentBy(memberUserDetails.getMember().getId(), 5));
        modelAndView.addObject("recentArticles",
                boardArticleService.searchRecentByCount(5));

        StatisticsCondition statisticsCondition = new StatisticsCondition();
        statisticsCondition.setStartDate(LocalDate.now().withDayOfMonth(1));
        statisticsCondition.setEndDate(LocalDate.now());

        modelAndView.addObject("visitorStatistics",
                statisticsService.searchVisitorStatistics(statisticsCondition));
        modelAndView.addObject("visitorCount", statisticsService.countTotalVisitor());

        modelAndView.addObject("boardArticleWeeks",
                statisticsService.searchBoardArticleStatistics(ChronoUnit.WEEKS));
        modelAndView.addObject("boardArticleMonths",
            statisticsService.searchBoardArticleStatistics(ChronoUnit.YEARS));


        return modelAndView;
    }

}
