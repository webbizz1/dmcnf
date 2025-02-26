package webbizz.crm.domain.surveyresponsemember.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class SurveyResponseMemberDto {

    /**
     * 참여자 응답 테이블 시퀀스
     */
    private Long id;

    /**
     * 설문 시퀀스
     */
    private Long surveyId;

    /**
     * 응답자 이름
     */
    private String respondentName;

    /**
     * 응답자 폰번호
     */
    private String respondentPhone;

    /**
     * 응답자 이메일
     */
    private String respondentEmail;

    /**
     * 자유의견
     */
    private String overallComment;

    /**
     * 등록일시
     */
    private LocalDateTime createdAt;

    /**
     * 선택한 문항에 대한 답들
     */
    @Setter
    private Map<Long, Map<Long, String>> questions;

}

