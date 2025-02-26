package webbizz.crm.domain.surveyquestionanswer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class SurveyQuestionAnswerSaveDto {

    /**
     * 시퀀스 음수 일시 등록
     */
    private Long answerId;

    /**
     * 문항의 답변 (custom_yn 이 Y 일시는 null)
     */
    @NotEmpty(message = "답변을 입력해주세요.")
    private String text;

    /**
     * 정렬 순서
     */
    @NotNull(message = "정렬 순서를 입력해주세요.")
    private Integer sortOrder;

}
