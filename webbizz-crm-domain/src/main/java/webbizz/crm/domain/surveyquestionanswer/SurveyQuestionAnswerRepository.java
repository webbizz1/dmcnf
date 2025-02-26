package webbizz.crm.domain.surveyquestionanswer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionAnswerRepository extends JpaRepository<SurveyQuestionAnswer, Long>,
        SurveyQuestionAnswerRepositoryCustom {
}
