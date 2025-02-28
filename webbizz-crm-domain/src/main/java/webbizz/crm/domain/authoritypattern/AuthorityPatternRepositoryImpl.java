package webbizz.crm.domain.authoritypattern;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.authoritypattern.dto.AuthorityPatternDto;
import webbizz.crm.domain.member.enumset.MemberRole;

import java.util.List;

import static webbizz.crm.domain.authoritypattern.entity.QAuthorityPattern.authorityPattern;
import static webbizz.crm.util.QuerydslUtils.condEq;

@Repository
@RequiredArgsConstructor
public class AuthorityPatternRepositoryImpl implements AuthorityPatternRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AuthorityPatternDto> searchAllByRole(final MemberRole role) {
        return queryFactory
                .select(Projections.fields(AuthorityPatternDto.class,
                        authorityPattern.id,
                        authorityPattern.parentId,
                        authorityPattern.name,
                        authorityPattern.urlPattern,
                        authorityPattern.depth,
                        authorityPattern.description,
                        authorityPattern.manageYn
                ))
                .from(authorityPattern)
                .where(
                        condEq(authorityPattern.role, role),
                        condEq(authorityPattern.manageYn, YN.Y)
                )
                .orderBy(
                        authorityPattern.depth.asc(),
                        authorityPattern.sortOrder.asc()
                )
                .fetch();
    }

}
