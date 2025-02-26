package webbizz.crm.web.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import webbizz.crm.domain.survey.dto.SurveyCondition;
import webbizz.crm.domain.survey.dto.SurveyDto;
import webbizz.crm.domain.survey.dto.SurveySaveDto;
import webbizz.crm.domain.survey.enumset.AnswerType;
import webbizz.crm.domain.surveyresponsedetail.dto.SurveyAnswerStatisticsDto;
import webbizz.crm.domain.surveyresponsedetail.dto.SurveyResponseDetailDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyOverallCommentDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyResponseResultDto;
import webbizz.crm.response.ApiResponse;
import webbizz.crm.service.excel.ExcelFile;
import webbizz.crm.service.survey.SurveyResponseService;
import webbizz.crm.service.survey.SurveyService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final SurveyResponseService surveyResponseService;

    /**
     * 콘텐츠 관리 - 민원 만족도 조사 - 게시물 목록
     *
     * @param condition 검색 조건
     * @param model     Model
     * @return 게시물 목록 화면
     * @author YONGHO LEE
     */
    @GetMapping("/survey")
    @PreAuthorize("hasPermission('/survey', 'GET')")
    public String contentsSurvey(@ModelAttribute("condition") final SurveyCondition condition, final Model model) {
        final Page<SurveyDto> surveyLists = surveyService.searchAllSurveysForAdmin(condition);
        model.addAttribute("list", surveyLists);

        return "survey/survey_list";
    }

    /**
     * 콘텐츠 관리 - 민원 만족도 조사 - 게시물 목록 - 등록
     *
     * @return 게시물 등록 화면
     * @author YONGHO LEE
     */
    @GetMapping("/survey/write")
    @PreAuthorize("hasPermission('/survey/write', 'GET')")
    public String contentsSurveyWrite() {
        return "survey/survey_write";
    }

    /**
     * 콘텐츠 관리 - 민원 만족도 조사 - 게시물 목록 - 상세
     *
     * @param id    설문 시퀀스
     * @param model Model
     * @return 게시물 상세 화면
     * @author YONGHO LEE
     */
    @GetMapping("/survey/{id}")
    @PreAuthorize("hasPermission('/survey', #id, 'GET')")
    public String contentsSurvey(@PathVariable(value = "id") final Long id, final Model model) {
        final SurveyDto survey = surveyService.findSurveyAndQuestionsAnswersById(id);
        final boolean isSurveyStarted = surveyService.isSurveyStarted(id);

        model.addAttribute("survey", survey);
        model.addAttribute("isSurveyStarted", isSurveyStarted);

        return "survey/survey_write";
    }

    /**
     * 콘텐츠 관리 - 민원 만족도 조사 - 게시물 목록 -   결과 보기
     *
     * @param surveyId 설문 시퀀스
     * @return 민원 만족도 조사 결과보기 화면
     * @author YONGHO LEE
     */
    @GetMapping("/survey/result/{surveyId}")
    @PreAuthorize("hasPermission('/survey/result', #surveyId, 'GET')")
    public String contentsSurveyResult(@PathVariable("surveyId") final Long surveyId,
                                       final Pageable pageable,
                                       final Model model
    ) {
        final SurveyResponseResultDto result = surveyResponseService.searchBySurveyResponseResult(surveyId, pageable);
        final SurveyDto surveyPopup = surveyService.findSurveyAndQuestionsAnswersById(surveyId);

        model.addAttribute("survey", result.getSurveyInfo());
        model.addAttribute("questions", result.getQuestions());
        model.addAttribute("list", result.getResultList());
        model.addAttribute("surveyPopup", surveyPopup);

        return "survey/survey_result";
    }

    /**
     * 콘텐츠 관리 - 민원 만족도 조사 - 통계 보기
     *
     * @param id 설문 시퀀스
     * @return 민원 만족도 조사 통계 보기 화면
     * @author YONGHO LEE
     */
    @GetMapping("/survey/{id}/statistics")
    @PreAuthorize("hasPermission('/survey/{id}/statistics', #id, 'GET')")
    public String contentsSurveyResult(@PathVariable("id") final Long id, final Model model) {
        final SurveyDto survey = surveyService.findSurveyAndQuestionsAnswersById(id);
        final Map<Long, Map<Long, SurveyAnswerStatisticsDto>> statistics = surveyResponseService.getSurveyStatistics(id);
        final List<SurveyOverallCommentDto> surveyMember = surveyResponseService.findOverallCommentsBySurveyId(id);

        model.addAttribute("survey", survey);
        model.addAttribute("statistics", statistics);
        model.addAttribute("surveyMember", surveyMember);

        return "survey/survey_statistics";
    }

    /**
     * 게시물 등록 API
     *
     * @param requestDto 게시물 저장 DTO
     * @author YONGHO LEE
     */
    @PostMapping("/api/v1/survey")
    @PreAuthorize("hasPermission('/survey', 'POST')")
    @ResponseBody
    public ApiResponse<Void> contentsSurveyPost(@RequestBody @Validated final SurveySaveDto requestDto) {
        surveyService.saveSurveys(requestDto);

        return ApiResponse.ok();
    }

    /**
     * 게시물 수정 API
     *
     * @param requestDto 게시물 저장 DTO
     * @return 게시물 시퀀스
     * @author YONGHO LEE
     */
    @PutMapping("/api/v1/survey")
    @PreAuthorize("hasPermission('/survey', 'PUT')")
    @ResponseBody
    public ApiResponse<Void> contentsSurveyPutV1(@RequestBody @Validated final SurveySaveDto requestDto) {
        surveyService.saveSurveys(requestDto);

        return ApiResponse.ok();
    }

    /**
     * 설문에 응답한 참여자 조회 API
     *
     * @param responseId 설문 응답 참여자 정보 시퀀스
     * @return 게시물 시퀀스
     * @author YONGHO LEE
     */
    @GetMapping("/api/v1/survey/response/{responseId}")
    @PreAuthorize("hasPermission('/survey/response/{responseId}', #responseId, 'GET')")
    @ResponseBody
    public ApiResponse<List<SurveyResponseDetailDto>> contentsSurveyResponse(@PathVariable final Long responseId) {
        return ApiResponse.ok(surveyResponseService.findSurveyResponseDetailByMemberId(responseId));
    }

    /**
     * 콘텐츠 관리 - 민원 만족도 조사 - 결과 보기 - 엑셀 다운로드
     *
     * @param id 설문 시퀀스
     * @return 엑셀 파일
     * @author YONGHO LEE
     */
    @PostMapping("/api/v1/survey/excel")
    @PreAuthorize("hasPermission('/survey/excel', #id, 'POST')")
    public ResponseEntity<?> contentsSurveyResultExcelPostV1(@Validated @RequestBody final Long id) {
        return new ExcelFile<>(surveyResponseService.searchSurveyResultExcelList(id)).toResponseEntity();
    }

    /**
     * enum AnswerType
     *
     * @return Map<String, Map < String, String>>
     * @author YONGHO LEE
     */
    @ModelAttribute("answerTypes")
    public Map<String, Map<String, String>> answerTypes() {
        return Arrays.stream(AnswerType.values())
                .filter(v -> v.getCode() != null)
                .collect(Collectors.toMap(
                        AnswerType::name,
                        an -> {
                            Map<String, String> stringStringMap = new HashMap<>();
                            stringStringMap.put("value", an.getCode().toLowerCase());
                            stringStringMap.put("code", an.getCode());
                            stringStringMap.put("label", an.getDetail());// detail == label
                            return stringStringMap;
                        }));
    }
}
