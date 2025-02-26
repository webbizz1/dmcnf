package webbizz.crm.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUpdatedBaseEntity is a Querydsl query type for UpdatedBaseEntity
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QUpdatedBaseEntity extends EntityPathBase<UpdatedBaseEntity> {

    private static final long serialVersionUID = -392659352L;

    public static final QUpdatedBaseEntity updatedBaseEntity = new QUpdatedBaseEntity("updatedBaseEntity");

    public final QSoftBaseEntity _super = new QSoftBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<YN> delYn = _super.delYn;

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath updatedId = createString("updatedId");

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QUpdatedBaseEntity(String variable) {
        super(UpdatedBaseEntity.class, forVariable(variable));
    }

    public QUpdatedBaseEntity(Path<? extends UpdatedBaseEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUpdatedBaseEntity(PathMetadata metadata) {
        super(UpdatedBaseEntity.class, metadata);
    }

}

