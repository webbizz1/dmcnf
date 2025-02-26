package webbizz.crm.domain.exhibition.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QExhibition is a Querydsl query type for Exhibition
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExhibition extends EntityPathBase<Exhibition> {

    private static final long serialVersionUID = -917563518L;

    public static final QExhibition exhibition = new QExhibition("exhibition");

    public final webbizz.crm.domain.QUpdatedBaseEntity _super = new webbizz.crm.domain.QUpdatedBaseEntity(this);

    public final EnumPath<webbizz.crm.domain.exhibition.enumset.BannerType> bannerType = createEnum("bannerType", webbizz.crm.domain.exhibition.enumset.BannerType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<webbizz.crm.domain.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<webbizz.crm.domain.exhibition.enumset.LinkTarget> linkTarget = createEnum("linkTarget", webbizz.crm.domain.exhibition.enumset.LinkTarget.class);

    public final StringPath linkUrl = createString("linkUrl");

    public final SimplePath<webbizz.crm.domain.attachment.dto.AttachmentDto> mobileImage = createSimple("mobileImage", webbizz.crm.domain.attachment.dto.AttachmentDto.class);

    public final SimplePath<webbizz.crm.domain.attachment.dto.AttachmentDto> pcImage = createSimple("pcImage", webbizz.crm.domain.attachment.dto.AttachmentDto.class);

    public final StringPath title = createString("title");

    public final EnumPath<webbizz.crm.domain.exhibition.enumset.ExhibitionType> type = createEnum("type", webbizz.crm.domain.exhibition.enumset.ExhibitionType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public final DateTimePath<java.time.LocalDateTime> viewEndAt = createDateTime("viewEndAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> viewOrder = createNumber("viewOrder", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> viewStartAt = createDateTime("viewStartAt", java.time.LocalDateTime.class);

    public final EnumPath<webbizz.crm.domain.YN> viewYn = createEnum("viewYn", webbizz.crm.domain.YN.class);

    public QExhibition(String variable) {
        super(Exhibition.class, forVariable(variable));
    }

    public QExhibition(Path<? extends Exhibition> path) {
        super(path.getType(), path.getMetadata());
    }

    public QExhibition(PathMetadata metadata) {
        super(Exhibition.class, metadata);
    }

}

