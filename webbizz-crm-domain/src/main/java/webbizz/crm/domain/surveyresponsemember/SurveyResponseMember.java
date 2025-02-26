package webbizz.crm.domain.surveyresponsemember;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;
import webbizz.crm.domain.UpdatedBaseEntity;
import webbizz.crm.domain.survey.Survey;
import webbizz.crm.domain.survey.enumset.AnswerType;
import webbizz.crm.domain.surveyquestion.SurveyQuestion;
import webbizz.crm.domain.surveyquestionanswer.SurveyQuestionAnswer;
import webbizz.crm.domain.surveyresponsedetail.SurveyResponseDetail;
import webbizz.crm.exception.ApiBadRequestException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(chain = true)
@Table(name = "survey_response_member")
public class SurveyResponseMember extends UpdatedBaseEntity {

    /**
     * 설문 응답 참여자 정보 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_response_member_id", nullable = false)
    private Long id;

    /**
     * 설문 시퀀스
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    /**
     * 회원 시퀀스 현재 (kind 회원이 없기에 미사용)
     */
    @Column(name = "member_id")
    private Long memberId;

    /**
     * 응답자 이름
     */
    @Size(max = 100)
    @NotNull
    @Column(name = "respondent_name", nullable = false, length = 100)
    private String respondentName;

    /**
     * 응답자 전화번호
     */
    @Size(max = 20)
    @NotNull
    @Column(name = "respondent_phone", nullable = false, length = 20)
    private String respondentPhone;

    /**
     * 응답자 이메일
     */
    @Size(max = 255)
    @NotNull
    @Column(name = "respondent_email", nullable = false)
    private String respondentEmail;

    /**
     * 응답 일시
     */
    @NotNull
    @Column(name = "response_date", nullable = false)
    private LocalDateTime responseDate;

    /**
     * 응답 IP 주소
     */
    @Size(max = 45)
    @NotNull
    @Column(name = "remote_addr", nullable = false, length = 45)
    private String remoteAddr;

    /**
     * 기타 느낀 점이나 의견 (자유 입력)
     */
    @Column(name = "overall_comment", columnDefinition = "TEXT")
    private String overallComment;

    @OneToMany(
            mappedBy = "surveyResponseMember",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    @Builder.Default
    private List<SurveyResponseDetail> responses = new ArrayList<>();

    public SurveyResponseMember(
            final Survey survey,
            final String respondentName,
            final String respondentPhone,
            final String respondentEmail,
            final String remoteAddr,
            final String overallComment
    ) {
        this(null, survey, null, respondentName, respondentPhone, respondentEmail,
                LocalDateTime.now(), remoteAddr, overallComment, new ArrayList<>());
    }

    /**
     * 설문 응답 상세 정보를 추가
     *
     * @param question         응답한 설문 문항
     * @param answer           선택한 답변 (객관식인 경우)
     * @param customAnswerText 주관식 답변 텍스트
     */
    public void addResponse(
            final SurveyQuestion question,
            final SurveyQuestionAnswer answer,
            final String customAnswerText
    ) {
        final SurveyResponseDetail detail = SurveyResponseDetail.createResponseDetail(
                this, // this = 현재 SurveyResponseMember 객체
                question,
                answer,
                customAnswerText);

        this.responses.add(detail);
    }

    public void validate() {
        // 단일 선택 문항인데 두개를 고를시 에러
        this.responses.stream()
                .filter(detail -> detail.getSurveyQuestion().getAnswerType() == AnswerType.RADIO)
                .collect(Collectors.groupingBy(
                        SurveyResponseDetail::getSurveyQuestion,
                        Collectors.counting()))
                .forEach((question, count) -> {
                    if (count > 1) {
                        throw new ApiBadRequestException(
                                String.format("[%s] 문항은 단일 선택만 가능합니다.", question.getTitle()));
                    }
                });

        // 응답한 문항 ID 목록
        final Map<Long, SurveyResponseDetail> responseDetailMap = this.responses.stream()
                .collect(Collectors.toMap(
                        detail -> detail.getSurveyQuestion().getId(),
                        Function.identity(),
                        (existing, replacement) -> existing
                ));

        // 입력 안한 문항 조회 후 에러
        final String missingTitle = this.survey.getSurveyQuestions().stream()
                .filter(question -> !responseDetailMap.containsKey(question.getId()))
                .sorted(Comparator.comparing(SurveyQuestion::getSortOrder))
                .map(SurveyQuestion::getTitle)
                .findFirst().orElse(null);
//                .collect(Collectors.joining(", "));

        System.out.println("missingTitle = " + missingTitle);
        System.out.println("missingTitle = " + missingTitle);
        System.out.println("missingTitle = " + missingTitle);
        System.out.println("missingTitle = " + missingTitle);
        System.out.println("missingTitle = " + missingTitle);
        if (StringUtils.hasText(missingTitle)) {
            throw new ApiBadRequestException(String.format("[%s] 문항에 응답을 해주세요.", missingTitle));
        }

        // 응답한 질문의 ID 목록
        final Set<Long> validQuestionIds = survey.getSurveyQuestions().stream()
                .map(SurveyQuestion::getId)
                .collect(Collectors.toSet());

        // 설문에 맞지 않는 문항이 있을시 에러
        responseDetailMap.keySet().stream()
                .filter(responseId -> !validQuestionIds.contains(responseId))
                .findFirst()
                .ifPresent(id -> {
                    throw new ApiBadRequestException("설문에 맞지 않는 문항이 있습니다.");
                });
    }

}