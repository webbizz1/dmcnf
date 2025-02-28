package webbizz.crm.domain.surveyquestion.dto;

import webbizz.crm.domain.survey.enumset.AnswerType;
import webbizz.crm.domain.surveyquestionanswer.dto.SurveyQuestionAnswerDto;
import lombok.Getter;
import lombok.Setter;
import webbizz.crm.domain.YN;

import java.util.List;

@Getter
public class SurveyQuestionDto {

    /**
     * 시퀀스 음수 일시 등록
     */
    private Long id;

    /**
     * 설문 문항 제목
     */
    private String title;

    /**
     * 문항 선택 유형
     */
    private AnswerType answerType;

    /**
     * 정렬 순서
     */
    private Integer sortOrder;

    /**
     * 기타 항목 사용 여부
     */
    private YN useCustomAnswerYn;

    /**
     * 해당 문항의 대한 답변 리스트
     */
    @Setter
    private List<SurveyQuestionAnswerDto> answers;

}
