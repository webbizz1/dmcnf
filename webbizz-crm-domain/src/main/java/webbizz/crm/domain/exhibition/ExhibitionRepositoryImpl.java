package webbizz.crm.domain.exhibition;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.exhibition.dto.ExhibitionCondition;
import webbizz.crm.domain.exhibition.dto.ExhibitionDto;
import webbizz.crm.domain.exhibition.enumset.ExhibitionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static webbizz.crm.domain.exhibition.entity.QExhibition.exhibition;
import static webbizz.crm.util.QuerydslUtils.condEq;
import static webbizz.crm.util.QuerydslUtils.condEqOrAllFalse;

@Repository
@RequiredArgsConstructor
public class ExhibitionRepositoryImpl implements ExhibitionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * {@link ExhibitionDto} SELECT 필드
     */
    private final QBean<ExhibitionDto> selectExhibitionDtoFields = Projections.fields(ExhibitionDto.class,
            exhibition.id,
            exhibition.type,
            exhibition.bannerType,
            exhibition.title,
            exhibition.linkUrl,
            exhibition.linkTarget,
            exhibition.pcImage,
            exhibition.mobileImage,
            exhibition.viewStartAt,
            exhibition.viewEndAt,
            exhibition.viewOrder,
            exhibition.viewYn,
            exhibition.createdAt
    );

    @Override
    public Optional<ExhibitionDto> searchBy(final Long id, final ExhibitionType type) {
        return Optional.ofNullable(queryFactory
                .select(this.selectExhibitionDtoFields)
                .from(exhibition)
                .where(
                        condEqOrAllFalse(exhibition.id, id),
                        condEq(exhibition.type, type),
                        condEq(exhibition.delYn, YN.N)
                )
                .fetchFirst());
    }

    @Override
    public Page<ExhibitionDto> searchAllByCondition(final ExhibitionCondition condition) {
        List<ExhibitionDto> content = queryFactory
                .select(this.selectExhibitionDtoFields)
                .from(exhibition)
                .where(
                        condEqOrAllFalse(exhibition.type, condition.getType()),
                        condEq(exhibition.delYn, YN.N)
                )
                .orderBy(exhibition.id.desc())
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(exhibition.count())
                .from(exhibition)
                .where(
                        condEqOrAllFalse(exhibition.type, condition.getType()),
                        condEq(exhibition.delYn, YN.N)
                );

        return PageableExecutionUtils.getPage(content, condition.pageable(), countQuery::fetchCount);
    }

    /**
     * 사용자 팝업
     * @param condition 검색 조건
     * @author Dong-Joon Oh
     */
    @Override
    public List<ExhibitionDto> searchMainAllPopup(ExhibitionCondition condition) {
        return queryFactory
                .select(this.selectExhibitionDtoFields)
                .from(exhibition)
                .where(
                        condEqOrAllFalse(exhibition.type, condition.getType()),
                        condEq(exhibition.delYn, YN.N),
                        condEq(exhibition.viewYn, YN.Y)
                )
                .orderBy(exhibition.id.desc())
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .fetch();
    }

    @Override
    public List<ExhibitionDto> searchAllForActive() {
        return queryFactory
                .select(this.selectExhibitionDtoFields)
                .from(exhibition)
                .where(
                        condEq(exhibition.viewYn, YN.Y),
                        condEq(exhibition.delYn, YN.N),
                        condViewAtIsActive()
                )
                .orderBy(
                        exhibition.type.asc(),
                        exhibition.viewOrder.asc()
                )
                .fetch();
    }

    @Override
    public Long countExhibitionForActive(final ExhibitionType type) {
        return queryFactory
                .select(Wildcard.count.coalesce(0L))
                .from(exhibition)
                .where(
                        condEq(exhibition.type, type),
                        condEq(exhibition.viewYn, YN.Y),
                        condEq(exhibition.delYn, YN.N),
                        condViewAtIsActive()
                )
                .fetchFirst();
    }




    private Predicate condViewAtIsActive() {
        return exhibition.viewStartAt.loe(LocalDateTime.now())
                .and(exhibition.viewEndAt.isNull()
                        .or(exhibition.viewEndAt.goe(LocalDateTime.now())));
    }

}
