package webbizz.crm.web.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.domain.member.enumset.MemberAuditorType;
import webbizz.crm.domain.statistics.dto.StatisticsCondition;
import webbizz.crm.domain.statistics.dto.StatisticsMemberDto;
import webbizz.crm.domain.statistics.dto.StatisticsOperatorCondition;
import webbizz.crm.response.ApiResponse;
import webbizz.crm.service.statistics.StatisticsService;
import webbizz.crm.util.PatternUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Controller
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/statistics")
    public String statistics(@AuthenticationPrincipal MemberUserDetails memberUserDetails, HttpServletRequest request) {
        return "redirect:" + memberUserDetails.getFirstMappingUrl(request, "/statistics/join-member");
    }

    /**
     * 통계·분석 - 회원 통계
     *
     * @param condition 검색 조건
     * @param model Model
     * @return 회원 통계 페이지
     * @author TAEROK HWANG
     */
    @GetMapping("/statistics/join-member")
    @PreAuthorize("hasPermission('/statistics/join-member', 'GET')")
    public String statisticsJoinMember(@ModelAttribute("condition") StatisticsCondition condition, Model model) {
        model.addAttribute("dto", statisticsService.searchMemberJoinStatistics(condition));
        return "statistics/statistics_member";
    }

    /**
     * 통계·분석 - 방문자 통계
     *
     * @param condition 검색 조건
     * @param model Model
     * @return 방문자 통계 페이지
     * @author TAEROK HWANG
     */
    @GetMapping("/statistics/visitor")
    @PreAuthorize("hasPermission('/statistics/visitor', 'GET')")
    public String statisticsVisitor(@ModelAttribute("condition") StatisticsCondition condition, Model model) {
        model.addAttribute("dto", statisticsService.searchVisitorStatistics(condition));
        return "statistics/statistics_visitor";
    }

    @GetMapping("/statistics/operator")
    public String statisticsOperator() {
        return "redirect:/statistics/operator/login-auditor";
    }

    /**
     * 통계·분석 - 운영자 로그 - 운영자 접속 현황
     *
     * @param condition 검색 조건
     * @param model Model
     * @return 운영자 접속 현황 페이지
     * @author TAEROK HWANG
     */
    @GetMapping("/statistics/operator/login-auditor")
    @PreAuthorize("hasPermission('/statistics/operator/login-auditor', 'GET')")
    public String statisticsOperatorLoginAuditor(@ModelAttribute("condition") StatisticsOperatorCondition condition,
                                                 Model model) {

        condition.setType(MemberAuditorType.LOGIN);

        model.addAttribute("pageVars", statisticsService.searchOperatorStatistics(condition));
        return "statistics/statistics_auditor_login";
    }

    /**
     * 통계·분석 - 운영자 로그 - 정보 수정 현황
     *
     * @param condition 검색 조건
     * @param model Model
     * @return 정보 수정 현황 페이지
     * @author TAEROK HWANG
     */
    @GetMapping("/statistics/operator/modification-auditor")
    @PreAuthorize("hasPermission('/statistics/operator/modification-auditor', 'GET')")
    public String statisticsOperatorModificationAuditor(@ModelAttribute("condition") StatisticsOperatorCondition condition,
                                                        Model model) {

        condition.setType(MemberAuditorType.MODIFICATION);

        model.addAttribute("pageVars", statisticsService.searchOperatorStatistics(condition));
        return "statistics/statistics_auditor_modification";
    }


    /**
     * 회원 통계 엑셀 다운로드
     *
     * @param condition 검색 조건
     * @return 엑셀 파일 다운로드 응답
     * @author TAEROK HWANG
     */
    @GetMapping("/api/v1/statistics/join-member/excel-download")
    @PreAuthorize("hasPermission('/api/v1/statistics/join-member', 'GET')")
    public ResponseEntity<byte[]> statisticsJoinMemberExcelDownload(StatisticsCondition condition) {
        return statisticsService.searchMemberJoinStatisticsForExcel(condition).toResponseEntity();
    }

    /**
     * 방문자 통계 엑셀 다운로드
     *
     * @param condition 검색 조건
     * @return 엑셀 파일 다운로드 응답
     * @author TAEROK HWANG
     */
    @GetMapping("/api/v1/statistics/visitor/excel-download")
    @PreAuthorize("hasPermission('/api/v1/statistics/visitor', 'GET')")
    public ResponseEntity<byte[]> statisticsVisitorMemberExcelDownload(StatisticsCondition condition) {
        return statisticsService.searchVisitorStatisticsForExcel(condition).toResponseEntity();
    }

    /**
     * 방문자 통계 차트 데이터 조회
     *
     * @param date 조회 날짜 (기준 월)
     * @return 방문자 통계 차트 데이터
     * @author TAEROK HWANG
     */
    @GetMapping("/api/v1/statistics/visitor-chart")
    @ResponseBody
    public ApiResponse<StatisticsMemberDto> statisticsVisitorChart(@RequestParam(name = "date", required = false)
                                                                   @DateTimeFormat(pattern = PatternUtils.DATE_FORMAT)
                                                                   LocalDate date) {

        StatisticsCondition condition = new StatisticsCondition();
        condition.setStartDate(date.with(TemporalAdjusters.firstDayOfMonth()));
        condition.setEndDate(date.with(TemporalAdjusters.lastDayOfMonth()));

        return ApiResponse.ok(statisticsService.searchVisitorStatistics(condition));
    }

    /**
     * 운영자 접속 현황 엑셀 다운로드
     *
     * @param condition 검색 조건
     * @return 엑셀 파일 다운로드 응답
     * @author TAEROK HWANG
     */
    @GetMapping("/api/v1/statistics/login-auditor/excel-download")
    @PreAuthorize("hasPermission('/api/v1/statistics/login-auditor', 'GET')")
    public ResponseEntity<byte[]> statisticsLoginAuditorExcelDownload(StatisticsOperatorCondition condition) {
        condition.setType(MemberAuditorType.LOGIN);
        return statisticsService.searchOperatorStatisticsForExcel(condition).toResponseEntity();
    }

    /**
     * 정보 수정 현황 엑셀 다운로드
     *
     * @param condition 검색 조건
     * @return 엑셀 파일 다운로드 응답
     * @author TAEROK HWANG
     */
    @GetMapping("/api/v1/statistics/modification-auditor/excel-download")
    @PreAuthorize("hasPermission('/api/v1/statistics/modification-auditor', 'GET')")
    public ResponseEntity<byte[]> statisticsModificationAuditorExcelDownload(StatisticsOperatorCondition condition) {
        condition.setType(MemberAuditorType.MODIFICATION);
        return statisticsService.searchOperatorStatisticsForExcel(condition).toResponseEntity();
    }

}
