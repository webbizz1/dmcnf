package webbizz.crm.domain.memberauditor.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberAuditor is a Querydsl query type for MemberAuditor
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberAuditor extends EntityPathBase<MemberAuditor> {

    private static final long serialVersionUID = 270048362L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberAuditor memberAuditor = new QMemberAuditor("memberAuditor");

    public final webbizz.crm.domain.QBaseEntity _super = new webbizz.crm.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final webbizz.crm.domain.member.entity.QMember member;

    public final StringPath remoteAddr = createString("remoteAddr");

    public final webbizz.crm.domain.member.entity.QMember targetMember;

    public final EnumPath<webbizz.crm.domain.member.enumset.MemberAuditorType> type = createEnum("type", webbizz.crm.domain.member.enumset.MemberAuditorType.class);

    public QMemberAuditor(String variable) {
        this(MemberAuditor.class, forVariable(variable), INITS);
    }

    public QMemberAuditor(Path<? extends MemberAuditor> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberAuditor(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberAuditor(PathMetadata metadata, PathInits inits) {
        this(MemberAuditor.class, metadata, inits);
    }

    public QMemberAuditor(Class<? extends MemberAuditor> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new webbizz.crm.domain.member.entity.QMember(forProperty("member")) : null;
        this.targetMember = inits.isInitialized("targetMember") ? new webbizz.crm.domain.member.entity.QMember(forProperty("targetMember")) : null;
    }

}

