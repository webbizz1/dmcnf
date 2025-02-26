package webbizz.crm.domain.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.ObjectUtils;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.survey.Survey;
import webbizz.crm.domain.surveyquestion.dto.SurveyQuestionDto;
import webbizz.crm.exception.BusinessException;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class SurveyDto {

    /**
     * 시퀀스
     */
    private Long id;

    /**
     * 제목
     */
    private String title;

    /**
     * 조사 시작일
     */
    private LocalDate startDate;

    /**
     * 조사 종료일
     */
    private LocalDate endDate;

    /**
     * 노출 여부
     */
    private YN viewYn;

    /**
     * 문항 정보 영역
     */
    private List<SurveyQuestionDto> questions;

    public SurveyDto(final Long id,
                     final String title,
                     final LocalDate startDate,
                     final LocalDate endDate,
                     final YN viewYn
    ) {
        this(id, title, startDate, endDate, viewYn, null);
    }

    /**
     * status -> 대기, 종료, 진행
     */
    public String getStatus() {
        final LocalDate today = LocalDate.now();

        if (this.startDate != null && this.endDate != null) {
            if (today.isBefore(this.startDate)) {
                return "대기";
            } else if (today.isAfter(this.endDate)) {
                return "종료";
            } else {
                return "진행";
            }
        }
        return "오류 (데이터 불일치)";
    }

    public static SurveyDto from(final Survey survey) {
        return new SurveyDto(
                survey.getId(),
                survey.getTitle(),
                survey.getStartDate(),
                survey.getEndDate(),
                survey.getViewYn()
        );
    }

    public static SurveyDto from(final SurveyDto surveyDto, final List<SurveyQuestionDto> questions) {
        return new SurveyDto(
                surveyDto.getId(),
                surveyDto.getTitle(),
                surveyDto.getStartDate(),
                surveyDto.getEndDate(),
                surveyDto.getViewYn(),
                questions
        );
    }

    public void validate() {
        if (!ObjectUtils.nullSafeEquals("진행", this.getStatus()))
            throw new BusinessException(404, "이 설문은 참여 불가능합니다.");
    }
}
