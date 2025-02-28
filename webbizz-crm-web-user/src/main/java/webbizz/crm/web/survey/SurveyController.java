package webbizz.crm.web.survey;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import webbizz.crm.domain.survey.dto.SurveyCondition;
import webbizz.crm.domain.survey.dto.SurveyDto;
import webbizz.crm.domain.survey.enumset.AnswerType;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyResponseDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyStep1Dto;
import webbizz.crm.response.ApiResponse;
import webbizz.crm.service.survey.SurveyResponseService;
import webbizz.crm.service.survey.SurveyService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final SurveyResponseService surveyResponseService;

    /**
     * 민원 만족도 조사 목록 조회
     *
     * @param model Model
     * @return 만조도 조사 목록
     * @author YONGHO LEE
     */
    @GetMapping("/survey")
    public String survey(@ModelAttribute(value = "condition") SurveyCondition condition, Model model) {
        Page<SurveyDto> surveyLists = surveyService.searchAllSurveysForUser(condition);
        model.addAttribute("list", surveyLists);

        return "survey/survey_list";
    }

    /**
     * 민원 만족도 조사 참여하기 step1 step2
     *
     * @return 만조도 조사 참여하는 페이지
     * @author YONGHO LEE
     */
    @GetMapping("/survey/{surveyId}")
    public ModelAndView surveyStep1(@PathVariable(value = "surveyId") Long surveyId,
                                    @RequestParam(value = "token", required = false) String token
    ) {
        return surveyService.surveyResponseModelAndViewForUser(surveyId, token);
    }

    /**
     * 민원 만족도 조사 참여하기 step1 (만조도 조사 참여자 정보 입력하는 화면)
     *
     * @return 참여자 정보가 담긴 jwt 토큰
     * @author YONGHO LEE
     */
    @PostMapping("/api/v1/survey/step1")
    @ResponseBody
    public ApiResponse<String> surveyStep1PostV1(@RequestBody SurveyStep1Dto requestDto) {
        return ApiResponse.ok(surveyService.createSurveyStep1Token(requestDto));
    }

    /**
     * 민원 만족도 조사 참여하기 step2 (저장)
     *
     * @return 설문 시퀀스
     * @author YONGHO LEE
     */
    @PostMapping("/api/v1/survey/response")
    @ResponseBody
    public ApiResponse<Long> surveyResponsePostV1(@Valid @RequestBody SurveyResponseDto requestDto) {
        return ApiResponse.ok(surveyResponseService.saveSurveyResponses(requestDto));
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
                            Map<String, String> stringStringMap = new java.util.HashMap<>();
                            stringStringMap.put("value", an.getCode().toLowerCase());
                            stringStringMap.put("code", an.getCode());
                            stringStringMap.put("label", an.getDetail());// detail == label
                            return stringStringMap;
                        }));
    }

}
