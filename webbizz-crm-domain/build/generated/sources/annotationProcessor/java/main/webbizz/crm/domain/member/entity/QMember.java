package webbizz.crm.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1510576424L;

    public static final QMember member = new QMember("member1");

    public final webbizz.crm.domain.QUpdatedBaseEntity _super = new webbizz.crm.domain.QUpdatedBaseEntity(this);

    public final ListPath<String, StringPath> allowIpAddresses = this.<String, StringPath>createList("allowIpAddresses", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<webbizz.crm.domain.memberauthority.entity.MemberAuthority, webbizz.crm.domain.memberauthority.entity.QMemberAuthority> authorities = this.<webbizz.crm.domain.memberauthority.entity.MemberAuthority, webbizz.crm.domain.memberauthority.entity.QMemberAuthority>createList("authorities", webbizz.crm.domain.memberauthority.entity.MemberAuthority.class, webbizz.crm.domain.memberauthority.entity.QMemberAuthority.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<webbizz.crm.domain.YN> delYn = _super.delYn;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastLoginAt = createDateTime("lastLoginAt", java.time.LocalDateTime.class);

    public final StringPath loginId = createString("loginId");

    public final StringPath mobileNumber = createString("mobileNumber");

    public final StringPath password = createString("password");

    public final StringPath realName = createString("realName");

    public final EnumPath<webbizz.crm.domain.member.enumset.MemberRole> role = createEnum("role", webbizz.crm.domain.member.enumset.MemberRole.class);

    public final EnumPath<webbizz.crm.domain.member.enumset.MemberStatus> status = createEnum("status", webbizz.crm.domain.member.enumset.MemberStatus.class);

    public final StringPath telephoneNumber = createString("telephoneNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public final DateTimePath<java.time.LocalDateTime> withdrawalAt = createDateTime("withdrawalAt", java.time.LocalDateTime.class);

    public final StringPath withdrawalReason = createString("withdrawalReason");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

