package webbizz.crm.domain.surveyresponsedetail;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyResponseDetailRepository extends JpaRepository<SurveyResponseDetail, Long>,
        SurveyResponseDetailRepositoryCustom {
}
