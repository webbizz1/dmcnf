package webbizz.crm.domain.memberauthority.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberAuthority is a Querydsl query type for MemberAuthority
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberAuthority extends EntityPathBase<MemberAuthority> {

    private static final long serialVersionUID = -1629081718L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberAuthority memberAuthority = new QMemberAuthority("memberAuthority");

    public final webbizz.crm.domain.QBaseEntity _super = new webbizz.crm.domain.QBaseEntity(this);

    public final webbizz.crm.domain.authoritypattern.entity.QAuthorityPattern authorityPattern;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final webbizz.crm.domain.member.entity.QMember member;

    public QMemberAuthority(String variable) {
        this(MemberAuthority.class, forVariable(variable), INITS);
    }

    public QMemberAuthority(Path<? extends MemberAuthority> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberAuthority(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberAuthority(PathMetadata metadata, PathInits inits) {
        this(MemberAuthority.class, metadata, inits);
    }

    public QMemberAuthority(Class<? extends MemberAuthority> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.authorityPattern = inits.isInitialized("authorityPattern") ? new webbizz.crm.domain.authoritypattern.entity.QAuthorityPattern(forProperty("authorityPattern")) : null;
        this.member = inits.isInitialized("member") ? new webbizz.crm.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

