package webbizz.crm.domain.surveyresponsedetail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponseDetailAnswerDto {

    /**
     * 질문 시퀀스
     */
    private Long questionId;

    /**
     * 응답자 이메일
     */
    private String email;

    /**
     * 응답자가 기타 입력 부분
     */
    private String text;

}

