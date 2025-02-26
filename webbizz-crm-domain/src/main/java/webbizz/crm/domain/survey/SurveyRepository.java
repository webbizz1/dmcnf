package webbizz.crm.domain.survey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import webbizz.crm.domain.survey.dto.SurveySaveDto;

import javax.validation.Valid;

public interface SurveyRepository extends JpaRepository<Survey, Long>, SurveyRepositoryCustom {

}
