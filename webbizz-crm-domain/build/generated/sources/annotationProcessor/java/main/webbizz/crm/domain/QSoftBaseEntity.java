package webbizz.crm.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSoftBaseEntity is a Querydsl query type for SoftBaseEntity
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QSoftBaseEntity extends EntityPathBase<SoftBaseEntity> {

    private static final long serialVersionUID = -559901883L;

    public static final QSoftBaseEntity softBaseEntity = new QSoftBaseEntity("softBaseEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    public final EnumPath<YN> delYn = createEnum("delYn", YN.class);

    public QSoftBaseEntity(String variable) {
        super(SoftBaseEntity.class, forVariable(variable));
    }

    public QSoftBaseEntity(Path<? extends SoftBaseEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSoftBaseEntity(PathMetadata metadata) {
        super(SoftBaseEntity.class, metadata);
    }

}

