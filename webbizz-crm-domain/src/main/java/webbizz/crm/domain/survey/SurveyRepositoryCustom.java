package webbizz.crm.domain.survey;

import org.springframework.data.domain.Page;
import webbizz.crm.domain.survey.dto.SurveyCondition;
import webbizz.crm.domain.survey.dto.SurveyDto;

import java.util.Optional;

public interface SurveyRepositoryCustom {

    Page<SurveyDto> searchAllSurveysForAdmin(final SurveyCondition condition);

    Page<SurveyDto> searchAllSurveysForUser(final SurveyCondition condition);

    Optional<SurveyDto> findBySurveyId(final Long id);

    /**
     * 활성화된 설문 수 조회
     *
     * @return 설문 수 (Long)
     * @author TAEROK HWANG
     */
    Long countSurveyForActive();

}
