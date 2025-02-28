package webbizz.crm.domain.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.domain.PageCondition;

@Getter
@AllArgsConstructor
public class SurveyCondition extends PageCondition {

    /**
     * 제목을 이용한 검색
     */
    private String searchText;

}
