package webbizz.crm.domain.member;

import com.querydsl.core.BooleanBuilder;
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
import org.springframework.util.StringUtils;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.member.dto.MemberAdminDto;
import webbizz.crm.domain.member.dto.MemberCondition;
import webbizz.crm.domain.member.dto.MemberDto;
import webbizz.crm.domain.member.enumset.MemberRole;

import java.util.List;
import java.util.Optional;

import static webbizz.crm.domain.member.entity.QMember.member;
import static webbizz.crm.util.QuerydslUtils.*;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /** {@link MemberDto} SELECT 필드 */
    private final QBean<MemberDto> selectMemberDtoFields = Projections.fields(MemberDto.class,
            member.id,
            member.loginId,
            member.status,
            member.realName,
            member.email,
            member.telephoneNumber,
            member.mobileNumber,
            member.lastLoginAt,
            member.withdrawalAt,
            member.withdrawalReason,
            member.createdAt
    );

    /** {@link MemberAdminDto} SELECT 필드 */
    private final QBean<MemberAdminDto> selectMemberAdminDtoFields = Projections.fields(MemberAdminDto.class,
            member.id,
            member.loginId,
            member.status,
            member.realName,
            member.email,
            member.telephoneNumber,
            member.mobileNumber,
            member.allowIpAddresses,
            member.lastLoginAt,
            member.createdAt,
            member.updatedAt
    );

    @Override
    public Optional<MemberDto> searchMemberById(final Long id) {
        return Optional.ofNullable(queryFactory
                .select(selectMemberDtoFields)
                .from(member)
                .where(member.id.eq(id))
                .fetchFirst());
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public Page<MemberDto> searchAllMemberByCondition(final MemberCondition condition) {
        List<MemberDto> content = queryFactory
                .select(selectMemberDtoFields)
                .from(member)
                .where(
                        condEqOrAllFalse(member.role, condition.getRole()),
                        condEq(member.status, condition.getStatus()),
                        condEq(member.delYn, condition.getDelYn()),
                        condDateBetween(member.createdAt, condition.getStartDate(), condition.getEndDate()),
                        condMultiFieldLike(condition.getSearchField(), condition.getSearchText())
                )
                .orderBy(member.id.desc())
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(member)
                .where(
                        condEqOrAllFalse(member.role, condition.getRole()),
                        condEq(member.status, condition.getStatus()),
                        condEq(member.delYn, condition.getDelYn()),
                        condDateBetween(member.createdAt, condition.getStartDate(), condition.getEndDate()),
                        condMultiFieldLike(condition.getSearchField(), condition.getSearchText())
                );

        return PageableExecutionUtils.getPage(content, condition.pageable(), countQuery::fetchCount);
    }

    @Override
    public Long countMemberForActive() {
        return queryFactory
                .select(Wildcard.count.coalesce(0L))
                .from(member)
                .where(
                        condEqOrAllFalse(member.role, MemberRole.USER),
                        condEq(member.delYn, YN.N)
                )
                .fetchFirst();
    }

    @Override
    public Optional<MemberAdminDto> searchMemberAdminById(final Long id) {
        return Optional.ofNullable(queryFactory
                .select(selectMemberAdminDtoFields)
                .from(member)
                .where(member.id.eq(id))
                .fetchFirst());
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public Page<MemberAdminDto> searchAllMemberAdminByCondition(final MemberCondition condition) {
        List<MemberAdminDto> content = queryFactory
                .select(selectMemberAdminDtoFields)
                .from(member)
                .where(
                        condEqOrAllFalse(member.role, condition.getRole()),
                        condEq(member.status, condition.getStatus()),
                        condEq(member.delYn, condition.getDelYn()),
                        condDateBetween(member.createdAt, condition.getStartDate(), condition.getEndDate()),
                        condMultiFieldLike(condition.getSearchField(), condition.getSearchText())
                )
                .orderBy(member.id.desc())
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(member)
                .where(
                        condEqOrAllFalse(member.role, condition.getRole()),
                        condEq(member.status, condition.getStatus()),
                        condEq(member.delYn, condition.getDelYn()),
                        condDateBetween(member.createdAt, condition.getStartDate(), condition.getEndDate()),
                        condMultiFieldLike(condition.getSearchField(), condition.getSearchText())
                );

        return PageableExecutionUtils.getPage(content, condition.pageable(), countQuery::fetchCount);
    }


    private Predicate condMultiFieldLike(final String searchField, final String searchText) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!StringUtils.hasText(searchField) || !StringUtils.hasText(searchText)) {
            return builder;
        }

        switch (searchField) {
            case "all":
                builder.or(condLike(member.loginId, searchText));
                builder.or(condLike(member.realName, searchText));
                builder.or(condLike(member.telephoneNumber, searchText.replace("-", "")));
                builder.or(condLike(member.mobileNumber, searchText.replace("-", "")));
                break;

            case "loginId":
                builder.or(condLike(member.loginId, searchText));
                break;

            case "realName":
                builder.or(condLike(member.realName, searchText));
                break;

            case "telephoneNumber":
                builder.or(condLike(member.telephoneNumber, searchText.replace("-", "")));
                break;

            case "mobileNumber":
                builder.or(condLike(member.mobileNumber, searchText.replace("-", "")));
                break;
        }

        return builder;
    }

}
