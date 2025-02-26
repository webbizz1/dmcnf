package webbizz.crm.domain.authoritypattern.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuthorityPattern is a Querydsl query type for AuthorityPattern
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthorityPattern extends EntityPathBase<AuthorityPattern> {

    private static final long serialVersionUID = 989301790L;

    public static final QAuthorityPattern authorityPattern = new QAuthorityPattern("authorityPattern");

    public final webbizz.crm.domain.QBaseEntity _super = new webbizz.crm.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    public final NumberPath<Integer> depth = createNumber("depth", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<webbizz.crm.domain.YN> manageYn = createEnum("manageYn", webbizz.crm.domain.YN.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final EnumPath<webbizz.crm.domain.member.enumset.MemberRole> role = createEnum("role", webbizz.crm.domain.member.enumset.MemberRole.class);

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final StringPath urlPattern = createString("urlPattern");

    public QAuthorityPattern(String variable) {
        super(AuthorityPattern.class, forVariable(variable));
    }

    public QAuthorityPattern(Path<? extends AuthorityPattern> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthorityPattern(PathMetadata metadata) {
        super(AuthorityPattern.class, metadata);
    }

}

