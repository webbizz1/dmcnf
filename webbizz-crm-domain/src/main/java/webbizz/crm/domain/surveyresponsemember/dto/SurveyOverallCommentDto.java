package webbizz.crm.domain.surveyresponsemember.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SurveyOverallCommentDto {

    private Long totalCount;

    private String email;

    private String overallComment;

}
