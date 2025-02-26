package webbizz.crm.service.statistics;

import org.springframework.data.domain.Page;
import webbizz.crm.domain.statistics.dto.*;
import webbizz.crm.service.excel.ExcelFile;

import java.time.temporal.ChronoUnit;
import java.util.List;

public interface StatisticsService {

    /**
     * 회원 통계 조회
     *
     * @param condition 검색 조건
     * @return 회원 통계 DTO
     * @author TAEROK HWANG
     */
    StatisticsMemberDto searchMemberJoinStatistics(StatisticsCondition condition);

    /**
     * 회원 통계 엑셀 다운로드
     *
     * @param condition 검색 조건
     * @return 엑셀 파일 객체
     * @author TAEROK HWANG
     */
    ExcelFile<?> searchMemberJoinStatisticsForExcel(StatisticsCondition condition);

    /**
     * 방문자 통계 조회
     *
     * @param condition 검색 조건
     * @return 회원 통계 DTO
     * @author TAEROK HWANG
     */
    StatisticsMemberDto searchVisitorStatistics(StatisticsCondition condition);

    /**
     * 방문자 통계 엑셀 다운로드
     *
     * @param condition 검색 조건
     * @return 엑셀 파일 객체
     * @author TAEROK HWANG
     */
    ExcelFile<?> searchVisitorStatisticsForExcel(StatisticsCondition condition);

    /**
     * 누적 방문자 수 조회
     *
     * @return 누적 방문자 수
     * @author TAEROK HWANG
     */
    Long countTotalVisitor();

    /**
     * 운영자 로그 조회
     *
     * @param condition 검색 조건
     * @return 운영자 로그 DTO 리스트 (Page)
     * @author TAEROK HWANG
     */
    Page<StatisticsOperatorDto> searchOperatorStatistics(StatisticsOperatorCondition condition);

    /**
     * 운영자 접속 현황 엑셀 다운로드
     *
     * @param condition 검색 조건
     * @return 엑셀 파일 객체
     * @author TAEROK HWANG
     */
    ExcelFile<?> searchOperatorStatisticsForExcel(StatisticsOperatorCondition condition);

    /**
     * 게시물 통계 조회
     *
     * @param unit 날짜 그룹 단위
     * @return 게시물 통계 DTO 리스트
     * @author TAEROK HWANG
     */
    List<StatisticsBoardArticleItemDto> searchBoardArticleStatistics(ChronoUnit unit);

}
