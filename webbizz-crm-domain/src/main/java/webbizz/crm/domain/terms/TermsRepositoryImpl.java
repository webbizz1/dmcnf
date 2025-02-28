package webbizz.crm.domain.terms;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.terms.dto.TermsCondition;
import webbizz.crm.domain.terms.dto.TermsDto;
import webbizz.crm.domain.terms.enumset.TermsType;

import java.util.List;
import java.util.Optional;

import static webbizz.crm.domain.terms.entity.QTerms.terms;
import static webbizz.crm.util.QuerydslUtils.condEq;
import static webbizz.crm.util.QuerydslUtils.condEqOrAllFalse;

@Repository
@RequiredArgsConstructor
public class TermsRepositoryImpl implements TermsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /** {@link TermsDto} SELECT 필드 */
    private final QBean<TermsDto> selectTermsDtoFields = Projections.fields(TermsDto.class,
            terms.id,
            terms.type,
            terms.content,
            terms.activeYn,
            terms.createdAt,
            terms.updatedAt
    );

    @Override
    public Optional<TermsDto> searchBy(final Long id, final TermsType type) {
        return Optional.ofNullable(queryFactory
                .select(this.selectTermsDtoFields)
                .from(terms)
                .where(
                        condEqOrAllFalse(terms.id, id),
                        condEq(terms.type, type),
                        condEq(terms.delYn, YN.N)
                )
                .fetchFirst());
    }

    @Override
    public Optional<TermsDto> searchByForActive(final TermsType type) {
        return Optional.ofNullable(queryFactory
                .select(this.selectTermsDtoFields)
                .from(terms)
                .where(
                        condEq(terms.type, type),
                        condEq(terms.activeYn, YN.Y),
                        condEq(terms.delYn, YN.N)
                )
                .fetchFirst());
    }

    @Override
    public List<TermsDto> searchAllByCondition(final TermsCondition condition) {
        return queryFactory
                .select(this.selectTermsDtoFields)
                .from(terms)
                .where(
                        condEqOrAllFalse(terms.type, condition.getType()),
                        condEq(terms.delYn, YN.N)
                )
                .orderBy(terms.id.desc())
                .fetch();
    }

}
