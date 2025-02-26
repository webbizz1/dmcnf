package webbizz.crm.domain.surveyquestionanswer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.UpdatedBaseEntity;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.surveyquestion.SurveyQuestion;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Accessors(chain = true)
@Table(name = "survey_question_answer")
public class SurveyQuestionAnswer extends UpdatedBaseEntity {

    /**
     * 설문 문항의 답변 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_question_answer_id", nullable = false)
    private Long id;

    /**
     * 설문 문항 시퀀스
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_question_id", nullable = false)
    private SurveyQuestion surveyQuestion;

    /**
     * 문항의 답변 (custom_yn 이 Y 일시는 null)
     */
    @Size(max = 255)
    @Column(name = "answer_text")
    private String answerText;

    /**
     * 정렬 순서
     */
    @NotNull
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    /**
     * 기타 입력 여부
     */
    @NotNull
    @Column(name = "custom_yn", columnDefinition = "CHAR", nullable = false)
    private YN customYn;

}