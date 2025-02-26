package webbizz.crm.domain.memberauthority;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static webbizz.crm.domain.memberauthority.entity.QMemberAuthority.memberAuthority;
import static webbizz.crm.util.QuerydslUtils.condEqOrAllFalse;

@Repository
@RequiredArgsConstructor
public class MemberAuthorityRepositoryImpl implements MemberAuthorityRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> searchPatternIdsByMemberId(final Long memberId) {
        return queryFactory
                .select(memberAuthority.authorityPattern.id)
                .from(memberAuthority)
                .where(condEqOrAllFalse(memberAuthority.member.id, memberId))
                .fetch();
    }

    @Override
    public void deleteByMemberId(final Long memberId) {
        queryFactory
                .delete(memberAuthority)
                .where(condEqOrAllFalse(memberAuthority.member.id, memberId))
                .execute();
    }

}
