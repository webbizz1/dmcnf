package webbizz.crm.domain.survey;

import lombok.AccessLevel;
import lombok.Builder;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Accessors(chain = true)
@Table(name = "survey")
public class Survey extends UpdatedBaseEntity {

    /**
     * 설문 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long id;

    /**
     * 설문 제목
     */
    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * 조사 시작일
     */
    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * 조사 종료일
     */
    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /**
     * 메뉴 노출 여부
     */
    @NotNull
    @Column(name = "view_yn", columnDefinition = "CHAR", nullable = false)
    private YN viewYn;

    /**
     * TODO 추후 삭제예정
     */
    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @Builder.Default
    private List<SurveyQuestion> surveyQuestions = new ArrayList<>();

    public void update(final String title, final LocalDate startDate, final LocalDate endDate, final YN viewYn) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.viewYn = viewYn;
    }

//    /**
//     * 노출 순서 정렬
//     */
//    public List<SurveyQuestion> getSortedQuestions() {
//        return surveyQuestions.stream()
//                .sorted(Comparator.comparing(SurveyQuestion::getSortOrder))
//                .collect(Collectors.toList());
//    }

}
