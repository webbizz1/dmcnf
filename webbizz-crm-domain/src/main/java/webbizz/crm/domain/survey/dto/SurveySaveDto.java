package webbizz.crm.domain.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.survey.Survey;
import webbizz.crm.domain.surveyquestion.dto.SurveyQuestionSaveDto;
import webbizz.crm.exception.ApiBadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class SurveySaveDto {

    /**
     * 설문 등록 정보 영역
     */
    @Valid
    private SurveyDto survey;

    /**
     * 문항 정보 영역
     */
    @Valid
    private List<SurveyQuestionSaveDto> questions;

    public SurveySaveDto(final SurveyDto survey) {
        this(survey, null);
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class SurveyDto {

        private Long id;

        /**
         * 설문 제목
         */
        @NotEmpty(message = "제목을 입력해주세요.")
        private String title;

        /**
         * 조사 시작일
         */
        @NotNull(message = "조사 시작일 입력해주세요.")
        private LocalDate startDate;

        /**
         * 조사 종료일
         */
        @NotNull(message = "조사 종료일 입력해주세요.")
        private LocalDate endDate;

        /**
         * 메뉴 노출 여부
         */
        @NotNull(message = "메뉴 노출 여부 입력해주세요.")
        private YN viewYn;

        public SurveyDto(String title, LocalDate startDate, LocalDate endDate, YN viewYn) {
            this(null, title, startDate, endDate, viewYn);
        }

        public void validateDates() {
            if (this.startDate.isAfter(this.endDate)) {
                throw new ApiBadRequestException("조사 시작일은 조사 종료일보다 이후일 수 없습니다.");
            } else {
                System.out.println("합격");
            }
        }
    }

    public void validate() {
        this.survey.validateDates();
        this.checkDuplicateQuestionTitle();
        this.questions.forEach(SurveyQuestionSaveDto::questionsValidate);
    }

    /**
     * 같은 질문이 있는지 중복을 체크합니다.
     */
    private void checkDuplicateQuestionTitle() {
        this.questions.stream().collect(Collectors.groupingBy(SurveyQuestionSaveDto::getTitle, Collectors.counting()))
                .forEach((title, count) -> {
                    if (count > 1)
                        throw new ApiBadRequestException(String.format("[%s] 질문이 중복되었습니다. ", title));
                });
    }

    /**
     * DTO entity 변환
     *
     * @return Survey 설문 엔티티
     */
    public Survey toEntity() {
        return Survey.builder()
                .title(this.survey.getTitle())
                .startDate(this.survey.getStartDate())
                .endDate(this.survey.getEndDate())
                .viewYn(this.survey.getViewYn())
                .build();
    }

}
