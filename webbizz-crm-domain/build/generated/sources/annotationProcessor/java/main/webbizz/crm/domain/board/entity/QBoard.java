package webbizz.crm.domain.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 651701450L;

    public static final QBoard board = new QBoard("board");

    public final webbizz.crm.domain.QUpdatedBaseEntity _super = new webbizz.crm.domain.QUpdatedBaseEntity(this);

    public final ListPath<webbizz.crm.domain.boardarticle.entity.BoardArticle, webbizz.crm.domain.boardarticle.entity.QBoardArticle> articles = this.<webbizz.crm.domain.boardarticle.entity.BoardArticle, webbizz.crm.domain.boardarticle.entity.QBoardArticle>createList("articles", webbizz.crm.domain.boardarticle.entity.BoardArticle.class, webbizz.crm.domain.boardarticle.entity.QBoardArticle.class, PathInits.DIRECT2);

    public final ListPath<webbizz.crm.domain.boardconfig.entity.BoardConfig, webbizz.crm.domain.boardconfig.entity.QBoardConfig> configs = this.<webbizz.crm.domain.boardconfig.entity.BoardConfig, webbizz.crm.domain.boardconfig.entity.QBoardConfig>createList("configs", webbizz.crm.domain.boardconfig.entity.BoardConfig.class, webbizz.crm.domain.boardconfig.entity.QBoardConfig.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<webbizz.crm.domain.YN> delYn = _super.delYn;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final EnumPath<webbizz.crm.domain.board.enumset.BoardType> type = createEnum("type", webbizz.crm.domain.board.enumset.BoardType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final EnumPath<webbizz.crm.domain.YN> useDescriptionYn = createEnum("useDescriptionYn", webbizz.crm.domain.YN.class);

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public final EnumPath<webbizz.crm.domain.YN> viewYn = createEnum("viewYn", webbizz.crm.domain.YN.class);

    public QBoard(String variable) {
        super(Board.class, forVariable(variable));
    }

    public QBoard(Path<? extends Board> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoard(PathMetadata metadata) {
        super(Board.class, metadata);
    }

}

