package webbizz.crm.domain.surveyresponsedetail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SurveyAnswerStatisticsDto {

    /**
     * 질문 시퀀스
     */
    private Long questionId;

    /**
     * 답변 시퀀스
     */
    private Long answerId;

    /**
     * 응답자가 해당 답변 고른 횟수
     */
    private Long answerCount = 0L;

    /**
     *  전체 참여자수
     */
    private Long totalResponses = 0L;

    /**
     * 비율
     */
    private Double ratio = 0.0d;

    /**
     * 사용자가 응답한 기타응답 목록
     */
    private List<SurveyResponseDetailAnswerDto> customAnswers = new ArrayList<>();

    public static SurveyAnswerStatisticsDto createStatistics(
            final Long questionId,
            final Long answerId,
            final Long answerCount,
            final Long totalResponses
    ) {
        SurveyAnswerStatisticsDto dto = new SurveyAnswerStatisticsDto();
        dto.questionId = questionId;
        dto.answerId = answerId;
        dto.answerCount = answerCount;
        dto.totalResponses = totalResponses;

        // 비율 계산 (소수점 첫째자리까지)
        double ratio = totalResponses > 0
                ? (double) answerCount / totalResponses * 100
                : 0.0;
        dto.ratio = Math.round(ratio * 10.0) / 10.0;

        return dto;
    }
}
