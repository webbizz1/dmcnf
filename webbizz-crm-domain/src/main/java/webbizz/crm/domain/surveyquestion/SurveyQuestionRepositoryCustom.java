package webbizz.crm.domain.surveyquestion;

import webbizz.crm.domain.surveyquestion.dto.SurveyQuestionDto;

import java.util.List;
import java.util.Optional;

public interface SurveyQuestionRepositoryCustom {

    Optional<List<SurveyQuestionDto>> findAllSurveyQuestionBySurveyId(final Long surveyId);

    /**
     * 해당 설문의 대한 질문의 시퀀스를 목록으로 반환
     * @param surveyId 설문 시퀀스
     * @return List<Long> survey_question_id 목록
     */
    List<Long> findQuestionIdsBySurveyId(final Long surveyId);

}
