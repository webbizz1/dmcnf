package webbizz.crm.domain.terms.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTerms is a Querydsl query type for Terms
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTerms extends EntityPathBase<Terms> {

    private static final long serialVersionUID = 1221999210L;

    public static final QTerms terms = new QTerms("terms");

    public final webbizz.crm.domain.QUpdatedBaseEntity _super = new webbizz.crm.domain.QUpdatedBaseEntity(this);

    public final EnumPath<webbizz.crm.domain.YN> activeYn = createEnum("activeYn", webbizz.crm.domain.YN.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<webbizz.crm.domain.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<webbizz.crm.domain.terms.enumset.TermsType> type = createEnum("type", webbizz.crm.domain.terms.enumset.TermsType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QTerms(String variable) {
        super(Terms.class, forVariable(variable));
    }

    public QTerms(Path<? extends Terms> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTerms(PathMetadata metadata) {
        super(Terms.class, metadata);
    }

}

