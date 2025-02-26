package webbizz.crm.domain.boardconfig.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardConfig is a Querydsl query type for BoardConfig
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardConfig extends EntityPathBase<BoardConfig> {

    private static final long serialVersionUID = 422646122L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardConfig boardConfig = new QBoardConfig("boardConfig");

    public final webbizz.crm.domain.QBaseEntity _super = new webbizz.crm.domain.QBaseEntity(this);

    public final webbizz.crm.domain.board.entity.QBoard board;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath key = createString("key");

    public final StringPath value = createString("value");

    public QBoardConfig(String variable) {
        this(BoardConfig.class, forVariable(variable), INITS);
    }

    public QBoardConfig(Path<? extends BoardConfig> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardConfig(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardConfig(PathMetadata metadata, PathInits inits) {
        this(BoardConfig.class, metadata, inits);
    }

    public QBoardConfig(Class<? extends BoardConfig> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new webbizz.crm.domain.board.entity.QBoard(forProperty("board")) : null;
    }

}

