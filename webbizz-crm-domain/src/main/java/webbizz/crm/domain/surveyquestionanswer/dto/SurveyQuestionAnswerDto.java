package webbizz.crm.domain.surveyquestionanswer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SurveyQuestionAnswerDto {

    /**
     * 시퀀스 음수 일시 등록
     */
    private Long answerId;

    /**
     * 질문 시퀀스
     */
    private Long surveyQuestionId;

    /**
     * 문항의 답변 (custom_yn 이 Y 일시는 null)
     */
    private String text;

    /**
     * 정렬 순서
     */
    private Integer sortOrder;
}
