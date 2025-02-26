package webbizz.crm.domain.surveyquestion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.survey.enumset.AnswerType;
import webbizz.crm.domain.surveyquestionanswer.dto.SurveyQuestionAnswerSaveDto;
import webbizz.crm.exception.ApiBadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class SurveyQuestionSaveDto {

    /**
     * 시퀀스 음수 일시 등록
     */
    private Long id;

    /**
     * 설문 문항 제목
     */
    @NotEmpty(message = "설문 문항 제목을 입력해주세요.")
    private String title;

    /**
     * 문항 선택 유형
     */
    private AnswerType answerType;

    /**
     * 정렬 순서
     */
    @NotNull(message = "정렬 순서를 입력해주세요.")
    private Integer sortOrder;

    /**
     * 기타 항목 사용 여부
     */
    @NotNull(message = "기타 항목 사용 여부를 입력해주세요.")
    private YN useCustomAnswerYn;

    /**
     * 해당 문항의 대한 답변 리스트
     */
    @NotNull(message = "해당 문항의 대한 답변을 입력해주세요.")
    @Valid
    private List<SurveyQuestionAnswerSaveDto> answers;

    public void questionsValidate() {
        this.checkDuplicateQuestionAnswerText();
        this.answerTypeValidate();
    }

    /**
     * 단일 선택 문항 검증
     */
    private void answerTypeValidate() {
        if (this.answerType == AnswerType.RADIO && useCustomAnswerYn.isBool()) {
            throw new ApiBadRequestException(String.format("[%s]단일 선택 문항은 기타 항목 입력이 불가능 합니다.", this.title));
        }
    }

    /**
     * 문항의 답변이 중복이 있는지 중복을 체크합니다.
     */
    private void checkDuplicateQuestionAnswerText() {
        this.answers.stream()
                .collect(Collectors.groupingBy(
                        SurveyQuestionAnswerSaveDto::getText, Collectors.counting())
                )
                .forEach((text, count) -> {
                    if (count > 1)
                        throw new ApiBadRequestException(
                                String.format("[%s]의 [%s] 문항이 중복이있습니다.", this.title, text));
                });
    }

}
