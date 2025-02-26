package webbizz.crm.domain.attachment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAttachment is a Querydsl query type for Attachment
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttachment extends EntityPathBase<Attachment> {

    private static final long serialVersionUID = -396562134L;

    public static final QAttachment attachment = new QAttachment("attachment");

    public final webbizz.crm.domain.QUpdatedBaseEntity _super = new webbizz.crm.domain.QUpdatedBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<webbizz.crm.domain.YN> delYn = _super.delYn;

    public final StringPath extension = createString("extension");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath originalName = createString("originalName");

    public final StringPath path = createString("path");

    public final StringPath realUrl = createString("realUrl");

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final StringPath source = createString("source");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final StringPath uploadName = createString("uploadName");

    public final StringPath uuid = createString("uuid");

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public final EnumPath<webbizz.crm.domain.YN> viewYn = createEnum("viewYn", webbizz.crm.domain.YN.class);

    public QAttachment(String variable) {
        super(Attachment.class, forVariable(variable));
    }

    public QAttachment(Path<? extends Attachment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAttachment(PathMetadata metadata) {
        super(Attachment.class, metadata);
    }

}

