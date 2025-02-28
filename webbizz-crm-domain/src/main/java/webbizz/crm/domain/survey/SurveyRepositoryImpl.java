package webbizz.crm.domain.survey;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.survey.dto.SurveyCondition;
import webbizz.crm.domain.survey.dto.SurveyDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static webbizz.crm.domain.survey.QSurvey.survey;
import static webbizz.crm.util.QuerydslUtils.condEq;
import static webbizz.crm.util.QuerydslUtils.condLike;

@Repository
@RequiredArgsConstructor
public class SurveyRepositoryImpl implements SurveyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SurveyDto> searchAllSurveysForAdmin(final SurveyCondition condition) {
        return searchAllSurveys(condition, this::adminConditions);
    }

    @Override
    public Page<SurveyDto> searchAllSurveysForUser(final SurveyCondition condition) {
        return searchAllSurveys(condition, this::userConditions);
    }

    private Page<SurveyDto> searchAllSurveys(final SurveyCondition condition,
                                             final Function<SurveyCondition, Predicate> conditionPredicate) {

        final List<SurveyDto> content = queryFactory
                .select(this.createSurveyDtoProjection())
                .from(survey)
                .where(conditionPredicate.apply(condition)) // 조건 빌더 호출
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .orderBy(survey.id.desc())
                .fetch();

        final JPAQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(survey)
                .where(conditionPredicate.apply(condition)); // 조건 빌더 호출

        return PageableExecutionUtils.getPage(content, condition.pageable(), countQuery::fetchOne);
    }

    @Override
    public Optional<SurveyDto> findBySurveyId(final Long id) {
        return Optional.of(
                queryFactory
                        .select(this.createSurveyDtoProjection())
                        .from(survey)
                        .where(condEq(survey.id, id))
                        .fetchFirst());
    }

    @Override
    public Long countSurveyForActive() {
        return queryFactory
                .select(Wildcard.count.coalesce(0L))
                .from(survey)
                .where(
                        condEq(survey.viewYn, YN.Y),
                        condEq(survey.delYn, YN.N),
                        condIsActive()
                )
                .fetchFirst();
    }

    /**
     * 관리자 검색
     *
     * @param condition 검색 조건
     * @return Predicate 관리자 검색 조건
     */
    private Predicate adminConditions(final SurveyCondition condition) {
        return new BooleanBuilder()
                .and(condLike(survey.title, condition.getSearchText()));
    }

    /**
     * 사용자 검색
     *
     * @param condition 검색 조건
     * @return Predicate 사용자 검색 조건
     */
    private Predicate userConditions(final SurveyCondition condition) {
        return new BooleanBuilder()
                .and(condLike(survey.title, condition.getSearchText()))
                .and(condEq(survey.viewYn, YN.Y));
    }

    private ConstructorExpression<SurveyDto> createSurveyDtoProjection() {
        return Projections.constructor(SurveyDto.class,
                survey.id,
                survey.title,
                survey.startDate,
                survey.endDate,
                survey.viewYn
        );
    }

    private Predicate condIsActive() {
        return survey.startDate.loe(LocalDate.now()).and(survey.endDate.goe(LocalDate.now()));
    }

}
