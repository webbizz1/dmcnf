package webbizz.crm.domain.surveyresponsedetail;

import webbizz.crm.domain.surveyresponsedetail.dto.SurveyAnswerStatisticsDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyOverallCommentDto;

import java.util.List;
import java.util.Map;

public interface SurveyResponseDetailRepositoryCustom {

    /**
     * 해당 설문의 맞는 통계 조회
     *
     * @param surveyId 설문 시퀀스
     */
    Map<Long, Map<Long, SurveyAnswerStatisticsDto>> getSurveyStatistics(final Long surveyId);

    /**
     * 설문의 기타자유 의견 목록을 조회 및 총 응답자 수
     *
     * @param surveyId 설문 시퀀스
     * @return 의견 목록 (총 응답자 수, 응답자 이메일, 전반적인 의견) 목록
     */
    List<SurveyOverallCommentDto> findOverallCommentsBySurveyId(final Long surveyId);



}
