package webbizz.crm.service.survey;

import org.springframework.data.domain.Page;
import org.springframework.web.servlet.ModelAndView;
import webbizz.crm.domain.survey.dto.SurveyCondition;
import webbizz.crm.domain.survey.dto.SurveyDto;
import webbizz.crm.domain.survey.dto.SurveySaveDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyStep1Dto;

public interface SurveyService {

    /**
     * 관리자 설문조사 목록 조회
     *
     * @param condition 검색 조건
     * @return 설문조사 DTO 리스트
     * @author YONGHO LEE
     */
    Page<SurveyDto> searchAllSurveysForAdmin(final SurveyCondition condition);

    /**
     * 사용자 설문조사 목록 조회
     *
     * @param condition 검색 조건
     * @return 설문조사 DTO 리스트
     * @author YONGHO LEE
     */
    Page<SurveyDto> searchAllSurveysForUser(final SurveyCondition condition);

     /**
     * 설문조사의 참여 페이지를 위한 ModelAndView 생성
     *
     * @param surveyId 설문 조사 퀀스
     * @param token 설문조사 응답 토큰
     * @return ModelAndView 객체
     *         - token 없는 경우: survey_response_step1 페이지로 이동
     *         - token 있는 경우: survey_response_step2 페이지로 이동
     * @author YONGHO LEE
     */
    ModelAndView surveyResponseModelAndViewForUser(final Long surveyId, final String token);

    /**
     * 설문조사의 ID로 해당 설문의 문항,답변 조회
     *
     * @param id 설문조사 시퀀스
     * @return 설문 조사 상세 DTO
     * @author YONGHO LEE
     */
    SurveyDto findSurveyAndQuestionsAnswersById(final Long id);

    /**
     * 활성화된 설문 수 조회
     *
     * @return 설문 수 (Long)
     * @author TAEROK HWANG
     */
    Long countSurveyForActive();

    /**
     * 설문조사 저장
     *
     * @param requestDto 저장 DTO
     * @author YONGHO LEE
     */
    void saveSurveys(final SurveySaveDto requestDto);

    /**
     * 특정 설문에 대한 사용자가 응답을 했는지 안했는지 확인
     *
     * @return boolean
     * @author YONGHO LEE
     */
    boolean isSurveyStarted(final Long surveyId);


    String createSurveyStep1Token(final SurveyStep1Dto dto);
}
