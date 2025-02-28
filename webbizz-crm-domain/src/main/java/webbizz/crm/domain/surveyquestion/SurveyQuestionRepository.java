package webbizz.crm.domain.surveyquestion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long>,
        SurveyQuestionRepositoryCustom {
}
