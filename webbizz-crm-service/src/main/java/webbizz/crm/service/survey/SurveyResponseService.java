package webbizz.crm.service.survey;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Pageable;
import webbizz.crm.domain.surveyresponsedetail.dto.SurveyAnswerStatisticsDto;
import webbizz.crm.domain.surveyresponsedetail.dto.SurveyResponseDetailDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyOverallCommentDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyResponseDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyResponseResultDto;

import java.util.List;
import java.util.Map;

public interface SurveyResponseService {

    /**
     * 만족조 조사 결과 보기 -> 리스트
     *
     * @param surveyId 설문 시퀀스
     * @param pageable 페이지 조건
     * @return SurveyResponseResultDto
     * @author YONGHO LEE
     */
    SurveyResponseResultDto searchBySurveyResponseResult(final Long surveyId, final Pageable pageable);

    /**
     * 설문 응답 저장
     *
     * @param requestDto 설문 응답 DTO
     * @return Long 저장된 시퀀스
     * @author YONGHO LEE
     */
    Long saveSurveyResponses(final SurveyResponseDto requestDto);

    /**
     * 해당설문의 응답한 것들을 조회
     *
     * @param responseMemberId 설문 응답 참여자 정보 시퀀
     * @return List<SurveyResponseDetailDto> 응답 상세 DTO
     * @author YONGHO LEE
     */
    List<SurveyResponseDetailDto> findSurveyResponseDetailByMemberId(final Long responseMemberId);

    /**
     * 해당 설문의 맞는 통계 조회
     *
     * @param surveyId 설문 시퀀스
     * @author YONGHO LEE
     */
    Map<Long, Map<Long, SurveyAnswerStatisticsDto>> getSurveyStatistics(final Long surveyId);

    /**
     * 설문의 기타자유 의견 목록을 조회 및 총 응답자 수
     *
     * @param surveyId 설문 시퀀스
     * @return 의견 목록 (총 응답자 수, 응답자 이메일, 전반적인 의견) 목록
     * @author YONGHO LEE
     */
    List<SurveyOverallCommentDto> findOverallCommentsBySurveyId(final Long surveyId);

    /**
     * 콘텐츠 관리 - 민원 만족도 조사 - 결과 보기 - 엑셀 다운로드
     *
     * @param surveyId 설문 시퀀스
     * @return 엑셀 파일
     * @author YONGHO LEE
     */
    Workbook searchSurveyResultExcelList(final Long surveyId);

}
