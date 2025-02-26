package webbizz.crm.domain.boardarticle.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardArticle is a Querydsl query type for BoardArticle
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardArticle extends EntityPathBase<BoardArticle> {

    private static final long serialVersionUID = 1430733156L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardArticle boardArticle = new QBoardArticle("boardArticle");

    public final webbizz.crm.domain.QUpdatedBaseEntity _super = new webbizz.crm.domain.QUpdatedBaseEntity(this);

    public final ListPath<webbizz.crm.domain.attachment.dto.AttachmentDto, SimplePath<webbizz.crm.domain.attachment.dto.AttachmentDto>> attachments = this.<webbizz.crm.domain.attachment.dto.AttachmentDto, SimplePath<webbizz.crm.domain.attachment.dto.AttachmentDto>>createList("attachments", webbizz.crm.domain.attachment.dto.AttachmentDto.class, SimplePath.class, PathInits.DIRECT2);

    public final webbizz.crm.domain.board.entity.QBoard board;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<webbizz.crm.domain.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<webbizz.crm.domain.attachment.dto.AttachmentDto, SimplePath<webbizz.crm.domain.attachment.dto.AttachmentDto>> images = this.<webbizz.crm.domain.attachment.dto.AttachmentDto, SimplePath<webbizz.crm.domain.attachment.dto.AttachmentDto>>createList("images", webbizz.crm.domain.attachment.dto.AttachmentDto.class, SimplePath.class, PathInits.DIRECT2);

    public final webbizz.crm.domain.member.entity.QMember member;

    public final EnumPath<webbizz.crm.domain.YN> noticeYn = createEnum("noticeYn", webbizz.crm.domain.YN.class);

    public final StringPath password = createString("password");

    public final SimplePath<webbizz.crm.domain.attachment.dto.AttachmentDto> pdf = createSimple("pdf", webbizz.crm.domain.attachment.dto.AttachmentDto.class);

    public final NumberPath<Integer> readCount = createNumber("readCount", Integer.class);

    public final EnumPath<webbizz.crm.domain.YN> secretYn = createEnum("secretYn", webbizz.crm.domain.YN.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public final EnumPath<webbizz.crm.domain.YN> viewYn = createEnum("viewYn", webbizz.crm.domain.YN.class);

    public final StringPath writerName = createString("writerName");

    public QBoardArticle(String variable) {
        this(BoardArticle.class, forVariable(variable), INITS);
    }

    public QBoardArticle(Path<? extends BoardArticle> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardArticle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardArticle(PathMetadata metadata, PathInits inits) {
        this(BoardArticle.class, metadata, inits);
    }

    public QBoardArticle(Class<? extends BoardArticle> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new webbizz.crm.domain.board.entity.QBoard(forProperty("board")) : null;
        this.member = inits.isInitialized("member") ? new webbizz.crm.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

