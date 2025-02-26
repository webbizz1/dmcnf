package webbizz.crm.domain.surveyquestion;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.UpdatedBaseEntity;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.survey.Survey;
import webbizz.crm.domain.survey.enumset.AnswerType;
import webbizz.crm.domain.surveyquestionanswer.SurveyQuestionAnswer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Accessors(chain = true)
@Table(name = "survey_question")
public class SurveyQuestion extends UpdatedBaseEntity {

    /**
     * 설문 문항 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_question_id", nullable = false)
    private Long id;

    /**
     * 설문 시퀀스
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    /**
     * 설문 문항 제목
     */
    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * 문항 선택 유형
     */
    @Column(name = "answer_type", nullable = false, length = 25)
    private AnswerType answerType;

    /**
     * 정렬 순서
     */
    @NotNull
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    /**
     * 기타 항목 사용 여부
     */
    @NotNull
    @Column(name = "use_custom_answer_yn", columnDefinition = "CHAR", nullable = false)
    private YN useCustomAnswerYn;

    /**
     * SurveyQuestionAnswer
     */
    @OneToMany(mappedBy = "surveyQuestion", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<SurveyQuestionAnswer> surveyQuestionAnswer = new ArrayList<>();

}