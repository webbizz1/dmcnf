package webbizz.crm.domain.surveyresponsedetail.dto;

import lombok.Getter;

@Getter
public class SurveyResponseDetailDto {

    /**
     * 참여자 응답 테이블 시퀀스
     */
    private Long responseMemberId;

    /**
     * 문항 시퀀스
     */
    private Long questionId;

    /**
     * 답변 시퀀스
     */
    private Long answerId;

    /**
     * 노출 순서
     */
    private Integer sortOrder;

    /**
     * 기타 입력
     */
    private String answerText;

}
