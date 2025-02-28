package webbizz.crm.domain.surveyresponsedetail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;
import webbizz.crm.domain.UpdatedBaseEntity;
import webbizz.crm.domain.survey.enumset.AnswerType;
import webbizz.crm.domain.surveyquestion.SurveyQuestion;
import webbizz.crm.domain.surveyquestionanswer.SurveyQuestionAnswer;
import webbizz.crm.domain.surveyresponsemember.SurveyResponseMember;
import webbizz.crm.exception.ApiBadRequestException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(chain = true)
@Table(name = "survey_response_detail")
public class SurveyResponseDetail extends UpdatedBaseEntity {

    /**
     * 설문 응답 상세 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_response_detail_id", nullable = false)
    private Long id;

    /**
     * 설문 응답 참여자 정보 시퀀스
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_response_member_id", nullable = false)
    private SurveyResponseMember surveyResponseMember;

    /**
     * 설문 문항
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_question_id", nullable = false)
    private SurveyQuestion surveyQuestion;

    /**
     * 선택한 답변 (기타 선택시 null)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_question_answer_id")
    private SurveyQuestionAnswer surveyQuestionAnswer;

    /**
     * 기타 응답 텍스트
     */
    @Column(name = "custom_answer_text", columnDefinition = "TEXT")
    private String customAnswerText;

    public SurveyResponseDetail(final SurveyResponseMember surveyResponseMember,
                                final SurveyQuestion question,
                                final SurveyQuestionAnswer answer,
                                final String customAnswerText
    ) {
        this(null, surveyResponseMember, question, answer, customAnswerText);
    }

    public static SurveyResponseDetail createResponseDetail(
            final SurveyResponseMember surveyResponseMember,
            final SurveyQuestion question,
            final SurveyQuestionAnswer answer,
            final String answerText
    ) {
        validateAll(question, answer, answerText);

        return new SurveyResponseDetail(surveyResponseMember, question, answer, answerText);
    }

    private static void validateAll(
            final SurveyQuestion surveyQuestion,
            final SurveyQuestionAnswer surveyQuestionAnswer,
            final String answerText
    ) {
        // 단일 선택형 검증
        if (surveyQuestion.getAnswerType() == AnswerType.RADIO)
            validateRadioTypeAnswer(surveyQuestion, surveyQuestionAnswer, answerText);

        // 답변 검증
        validateAnswers(surveyQuestion, surveyQuestionAnswer);

        // 주관식 답변 검증
        validateCustomAnswer(surveyQuestion, answerText);
    }

    private static void validateCustomAnswer(final SurveyQuestion question, final String answerText) {
        if (StringUtils.hasText(answerText) && !question.getUseCustomAnswerYn().isBool())
            throw new ApiBadRequestException(String.format("[%s] 이 문항은 기타입력이 존재하지 않습니다.", question.getTitle()));
    }

    private static void validateRadioTypeAnswer(
            final SurveyQuestion question,
            final SurveyQuestionAnswer answer,
            final String answerText
    ) {
        // 단일 선택형인데 기타입력이 있는 경우
        if (StringUtils.hasText(answerText)) {
            throw new ApiBadRequestException(String.format("[%s] 문항은 기타 입력이 존재하지 않습니다.", question.getTitle()));
        }

        // 단일 선택형인데 둘 다 없는 경우
        if (answer == null) {
            throw new ApiBadRequestException(String.format("[%s] 문항의 답을 입력해주세요.", question.getTitle()));
        }
    }

    /**
     * 해당질문의 대한 답변인지 검증
     *
     * @param surveyQuestion       설문의 대한 질문
     * @param surveyQuestionAnswer 설문 문항의 답변
     */
    private static void validateAnswers(final SurveyQuestion surveyQuestion,
                                        final SurveyQuestionAnswer surveyQuestionAnswer
    ) {
        Optional.ofNullable(surveyQuestionAnswer)
                .ifPresent(ans -> {
                    if (!surveyQuestion.getId().equals(ans.getSurveyQuestion().getId()))
                        throw new ApiBadRequestException("해당질문의 대한 답변이 아닙니다.");
                });
    }

}
