package webbizz.crm.service.statistics;

import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webbizz.crm.domain.statistics.StatisticsRepository;
import webbizz.crm.domain.statistics.dto.*;
import webbizz.crm.domain.visitorlog.VisitorLogRepository;
import webbizz.crm.service.excel.ExcelFile;
import webbizz.crm.service.statistics.dto.StatisticsMemberExcelDto;
import webbizz.crm.service.statistics.dto.StatisticsOperatorExcelDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("statisticsService")
@RequiredArgsConstructor
public class StatisticsServiceImpl extends EgovAbstractServiceImpl implements StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final VisitorLogRepository visitorLogRepository;

    @Override
    @Transactional(readOnly = true)
    public StatisticsMemberDto searchMemberJoinStatistics(final StatisticsCondition condition) {
        // 시작일, 종료일 조건 체크
        this.prepareDateCondition(condition, ChronoUnit.MONTHS);

        // 날짜 범위에 따른 날짜 리스트 생성
        List<LocalDate> labels = this.getLocalDateListForDay(condition, ChronoUnit.DAYS);

        // 통계 데이터 조회
        List<StatisticsMemberItemDto> rawDtos = statisticsRepository.searchMemberJoinStatistics(condition);

        // 통계 데이터 테이블 생성
        List<StatisticsMemberItemDto> items = StatisticsServiceImpl.getStatisticsTable(
                rawDtos,
                labels,
                StatisticsMemberItemDto::getDate,
                (date, value) -> new StatisticsMemberItemDto(date, value, BigDecimal.ZERO)
        );

        // 차트 데이터 생성
        List<ChartDatasetDto> datasets = new ArrayList<>();
        datasets.add(StatisticsServiceImpl.getDataset(items, "가입자 수", StatisticsMemberItemDto::getValue));

        // 결과 반환
        return new StatisticsMemberDto(items, labels, datasets);
    }

    @Override
    @Transactional(readOnly = true)
    public ExcelFile<?> searchMemberJoinStatisticsForExcel(final StatisticsCondition condition) {
        StatisticsMemberDto list = this.searchMemberJoinStatistics(condition);
        List<StatisticsMemberExcelDto> excelDto = new ArrayList<>();

        for (StatisticsMemberItemDto item : list.getItems()) {
            excelDto.add(new StatisticsMemberExcelDto(
                    item.getDate(),
                    item.getValue(),
                    item.getRatio().equals(BigDecimal.ZERO) ? null : item.getRatio()
            ));
        }

        return new ExcelFile<>(excelDto, "회원 통계", StatisticsMemberExcelDto.class);
    }

    @Override
    public Long countTotalVisitor() {
        return visitorLogRepository.countTotalVisitor();
    }

    @Override
    @Transactional(readOnly = true)
    public StatisticsMemberDto searchVisitorStatistics(final StatisticsCondition condition) {
        // 시작일, 종료일 조건 체크
        this.prepareDateCondition(condition, ChronoUnit.MONTHS);

        // 날짜 범위에 따른 날짜 리스트 생성
        List<LocalDate> labels = this.getLocalDateListForDay(condition, ChronoUnit.DAYS);

        // 통계 데이터 조회
        List<StatisticsMemberItemDto> rawDtos = statisticsRepository.searchVisitorStatistics(condition);

        // 통계 데이터 테이블 생성
        List<StatisticsMemberItemDto> items = StatisticsServiceImpl.getStatisticsTable(
                rawDtos,
                labels,
                StatisticsMemberItemDto::getDate,
                (date, value) -> new StatisticsMemberItemDto(date, value, BigDecimal.ZERO)
        );

        // 차트 데이터 생성
        List<ChartDatasetDto> datasets = new ArrayList<>();
        datasets.add(StatisticsServiceImpl.getDataset(items, "방문자 수", StatisticsMemberItemDto::getValue));

        // 결과 반환
        return new StatisticsMemberDto(items, labels, datasets);
    }

    @Override
    @Transactional(readOnly = true)
    public ExcelFile<?> searchVisitorStatisticsForExcel(final StatisticsCondition condition) {
        StatisticsMemberDto list = this.searchVisitorStatistics(condition);
        List<StatisticsMemberExcelDto> excelDto = new ArrayList<>();

        for (StatisticsMemberItemDto item : list.getItems()) {
            excelDto.add(new StatisticsMemberExcelDto(
                    item.getDate(),
                    item.getValue(),
                    item.getRatio().equals(BigDecimal.ZERO) ? null : item.getRatio()
            ));
        }

        return new ExcelFile<>(excelDto, "방문자 통계", StatisticsMemberExcelDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StatisticsOperatorDto> searchOperatorStatistics(final StatisticsOperatorCondition condition) {
        return statisticsRepository.searchOperatorStatistics(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public ExcelFile<?> searchOperatorStatisticsForExcel(final StatisticsOperatorCondition condition) {
        condition.setPage(1);
        condition.setSize(Integer.MAX_VALUE);
        Page<StatisticsOperatorDto> page = this.searchOperatorStatistics(condition);
        List<StatisticsOperatorDto> list = page.getContent();

        List<StatisticsOperatorExcelDto> excelDtos = list.stream()
                .map(item -> new StatisticsOperatorExcelDto(
                        page.getTotalElements() - (long) page.getNumber() * page.getSize() - list.indexOf(item),
                        item.getLoginId(),
                        item.getRealName(),
                        item.getRemoteAddr(),
                        item.getCreatedAt(),
                        item.getType().getDetail()
                ))
                .collect(Collectors.toList());

        return new ExcelFile<>(excelDtos, "운영자 접속 현황", StatisticsOperatorExcelDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatisticsBoardArticleItemDto> searchBoardArticleStatistics(final ChronoUnit unit) {
        StatisticsCondition condition = new StatisticsCondition();

        // 라벨 리스트 생성
        List<String> labels;
        switch (unit) {
            case WEEKS: { // 주간 - 일주일 데이터를 일별로 그룹
                condition.setStartDate(LocalDate.now().minusWeeks(1).plusDays(1));
                condition.setEndDate(LocalDate.now());

                labels = this.getLocalDateListForDay(condition, ChronoUnit.DAYS).stream()
                        .map(LocalDate::toString)
                        .collect(Collectors.toList());
                break;
            }
            case YEARS: { // 월간 - 1년 데이터를 월별로 그룹
                condition.setStartDate(LocalDate.now()
                        .minusYears(1)
                        .plusMonths(1)
                        .with(TemporalAdjusters.firstDayOfMonth()));
                condition.setEndDate(LocalDate.now());

                labels = this.getLocalDateListForDay(condition, ChronoUnit.MONTHS).stream()
                        .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM")))
                        .collect(Collectors.toList());
                break;
            }
            default:
                labels = new ArrayList<>();
        }

        // 통계 데이터 조회
        List<StatisticsBoardArticleItemDto> rawDtos =
                statisticsRepository.searchBoardArticleStatistics(condition, unit);

        // 통계 데이터 테이블 생성 후 결과 반환
        return StatisticsServiceImpl.getStatisticsTable(
                rawDtos,
                labels,
                StatisticsBoardArticleItemDto::getDate,
                StatisticsBoardArticleItemDto::new
        );
    }


    /**
     * 날짜 조건을 체크하고 없거나 범위를 벗어났을 경우 기본값으로 설정
     *
     * @param condition 통계 조회 조건
     * @param unit 날짜 단위
     * @author TAEROK HWANG
     */
    private void prepareDateCondition(final StatisticsCondition condition, final ChronoUnit unit) {
        // 시작일, 종료일 조건 체크
        if (condition.getStartDate() == null || condition.getStartDate().isAfter(LocalDate.now())) {
            condition.setStartDate(LocalDate.now().minusMonths(1).plusDays(1));
        }
        if (condition.getEndDate() == null || condition.getEndDate().isAfter(LocalDate.now())) {
            condition.setEndDate(LocalDate.now());
        }
        if (condition.getStartDate().isAfter(condition.getEndDate())) {
            condition.setStartDate(LocalDate.now().minus(1, unit));
            condition.setEndDate(LocalDate.now());
        }
    }

    /**
     * 날짜 범위에 따른 리스트 생성
     *
     * @param condition 통계 조회 조건
     * @param unit 날짜 그룹 단위
     * @return 날짜 리스트
     * @author TAEROK HWANG
     */
    private List<LocalDate> getLocalDateListForDay(final StatisticsCondition condition, final ChronoUnit unit) {
        long unitsBetween = unit.between(condition.getStartDate(), condition.getEndDate().plus(1, unit));
        return Stream.iterate(condition.getStartDate(), date -> date.plus(1, unit))
                .limit(unitsBetween)
                .collect(Collectors.toList());
    }


    /**
     * 통계 데이터 테이블 생성
     *
     * @param rawDtos 원본 데이터
     * @param labels 라벨 리스트 (날짜 또는 문자열)
     * @param labelExtractor 라벨 추출 함수
     * @param dtoCreator DTO 생성 함수
     * @return 통계 데이터 테이블
     * @param <T> DTO 타입
     * @param <L> 라벨 타입
     * @author TAEROK HWANG
     */
    private static <T, L> List<T> getStatisticsTable(final List<T> rawDtos,
                                                     final List<L> labels,
                                                     final Function<T, L> labelExtractor,
                                                     final BiFunction<L, BigDecimal, T> dtoCreator) {

        Map<L, T> dtoMap = rawDtos.stream()
                .collect(Collectors.toMap(labelExtractor, dto -> dto));

        return labels.stream()
                .map(label -> dtoMap.getOrDefault(label, dtoCreator.apply(label, BigDecimal.ZERO)))
                .collect(Collectors.toList());
    }

    /**
     * 날짜 범위에 따른 차트 데이터셋 생성
     *
     * @param statisticsTableDtos 통계 데이터 테이블 DTO
     * @param labelName 레이블 이름
     * @param valueExtractor 값 추출 함수
     * @return 차트 데이터셋
     * @param <T> 통계 테이블 DTO 타입
     * @author TAEROK HWANG
     */
    private static <T> ChartDatasetDto getDataset(final List<T> statisticsTableDtos,
                                                  final String labelName,
                                                  final Function<T, BigDecimal> valueExtractor) {

        ChartDatasetDto dataset = new ChartDatasetDto();
        dataset.setLabel(labelName);
        dataset.setData(statisticsTableDtos.stream()
                .map(valueExtractor)
                .collect(Collectors.toList()));

        return dataset;
    }

}
