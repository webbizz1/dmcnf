package webbizz.crm.domain.statistics;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.member.enumset.MemberRole;
import webbizz.crm.domain.statistics.dto.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static webbizz.crm.domain.boardarticle.entity.QBoardArticle.boardArticle;
import static webbizz.crm.domain.member.entity.QMember.member;
import static webbizz.crm.domain.memberauditor.entity.QMemberAuditor.memberAuditor;
import static webbizz.crm.domain.visitorlog.entity.QVisitorLog.visitorLog;
import static webbizz.crm.util.QuerydslUtils.*;

@Repository
@RequiredArgsConstructor
public class StatisticsRepository {

    /*
     * 통계 Repository 는 특정 도메인에 종속된 Repository 가 아니므로 JpaRepository 를 상속받지 않고 사용한다.
     * QueryDSL 을 사용하기 위해 JPAQueryFactory (EntityManager 로 생성된 JPAQueryFactory) 를 주입받아 사용한다.
     */

    private final JPAQueryFactory queryFactory;

    /**
     * 회원 통계 조회
     *
     * @param condition 검색 조건
     * @return 회원 통계 DTO 리스트
     * @author TAEROK HWANG
     */
    public List<StatisticsMemberItemDto> searchMemberJoinStatistics(final StatisticsCondition condition) {
        DateTemplate<Date> datePath = Expressions.dateTemplate(Date.class, "DATE({0})", member.createdAt);

        List<Tuple> tuple = queryFactory
                .select(
                        datePath,
                        Wildcard.count.castToNum(BigDecimal.class)
                )
                .from(member)
                .where(
                        condEqOrAllFalse(member.role, MemberRole.USER),
                        condDateBetween(member.createdAt, condition.getStartDate(), condition.getEndDate())
                )
                .groupBy(datePath)
                .fetch();

        BigDecimal totalValue = tuple.stream()
                .map(t -> t.get(1, BigDecimal.class))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return tuple.stream()
                .map(t -> new StatisticsMemberItemDto(
                        Optional.ofNullable(t.get(0, Date.class)).map(Date::toLocalDate).orElse(null),
                        t.get(1, BigDecimal.class),
                        Optional.ofNullable(t.get(1, BigDecimal.class)).orElse(BigDecimal.ZERO)
                                .divide(totalValue, 6, RoundingMode.HALF_UP)
                ))
                .collect(Collectors.toList());
    }

    /**
     * 방문자 통계 조회
     *
     * @param condition 검색 조건
     * @return 회원 통계 DTO 리스트
     * @author TAEROK HWANG
     */
    public List<StatisticsMemberItemDto> searchVisitorStatistics(final StatisticsCondition condition) {
        List<Tuple> tuple = queryFactory
                .select(
                        visitorLog.groupDate,
                        visitorLog.remoteAddr.countDistinct().castToNum(BigDecimal.class)
                )
                .from(visitorLog)
                .where(condDateBetween(visitorLog.groupDate, condition.getStartDate(), condition.getEndDate()))
                .groupBy(visitorLog.groupDate)
                .fetch();

        BigDecimal totalValue = tuple.stream()
                .map(t -> t.get(1, BigDecimal.class))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return tuple.stream()
                .map(t -> new StatisticsMemberItemDto(
                        t.get(0, LocalDate.class),
                        t.get(1, BigDecimal.class),
                        Optional.ofNullable(t.get(1, BigDecimal.class)).orElse(BigDecimal.ZERO)
                                .divide(totalValue, 6, RoundingMode.HALF_UP)
                ))
                .collect(Collectors.toList());
    }

    /**
     * 운영자 로그 조회
     *
     * @param condition 검색 조건
     * @return 운영자 로그 DTO 리스트 (Page)
     * @author TAEROK HWANG
     */
    public Page<StatisticsOperatorDto> searchOperatorStatistics(final StatisticsOperatorCondition condition) {
        List<StatisticsOperatorDto> content = queryFactory
                .select(Projections.fields(StatisticsOperatorDto.class,
                        member.loginId,
                        member.realName,
                        memberAuditor.remoteAddr,
                        memberAuditor.createdAt,
                        memberAuditor.type
                ))
                .from(memberAuditor)
                .leftJoin(memberAuditor.member, member)
                .where(
                        condEqOrAllFalse(memberAuditor.type, condition.getType()),
                        condDateBetween(member.createdAt, condition.getStartDate(), condition.getEndDate())
                )
                .orderBy(memberAuditor.id.desc())
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(memberAuditor)
                .leftJoin(memberAuditor.member, member)
                .where(
                        condEqOrAllFalse(memberAuditor.type, condition.getType()),
                        condDateBetween(member.createdAt, condition.getStartDate(), condition.getEndDate())
                );

        return PageableExecutionUtils.getPage(content, condition.pageable(), countQuery::fetchCount);
    }

    /**
     * 게시물 통계 조회
     *
     * @param condition 검색 조건
     * @param unit 날짜 그룹 단위
     * @return 게시물 통계 DTO 리스트
     * @author TAEROK HWANG
     */
    public List<StatisticsBoardArticleItemDto> searchBoardArticleStatistics(final StatisticsCondition condition,
                                                                            final ChronoUnit unit) {

        NumberTemplate<Long> yearPath = Expressions.numberTemplate(Long.class, "YEAR({0})", boardArticle.createdAt);
        NumberTemplate<Long> monthPath = Expressions.numberTemplate(Long.class, "MONTH({0})", boardArticle.createdAt);
        NumberTemplate<Long> datePath = Expressions.numberTemplate(Long.class, "DATE({0})", boardArticle.createdAt);

        // ANSI SQL 호환이 되는 방법으로 groupBy 구현
        StringExpression groupPath = unit == ChronoUnit.WEEKS
                ? datePath.stringValue()
                : yearPath.stringValue().concat("-").concat(monthPath.stringValue());

        List<Tuple> tuple = queryFactory
                .select(
                        groupPath,
                        Wildcard.count.castToNum(BigDecimal.class)
                )
                .from(boardArticle)
                .where(
                        condEq(boardArticle.delYn, YN.N),
                        condDateBetween(boardArticle.createdAt, condition.getStartDate(), condition.getEndDate())
                )
                .groupBy(groupPath)
                .orderBy(groupPath.desc())
                .fetch();

        List<StatisticsBoardArticleItemDto> collect = tuple.stream()
                .map(t -> new StatisticsBoardArticleItemDto(
                        String.valueOf(t.get(0, String.class)),
                        t.get(1, BigDecimal.class)
                ))
                .collect(Collectors.toList());
        Collections.reverse(collect);

        return collect;
    }

}
