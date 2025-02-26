package webbizz.crm.domain.visitorlog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVisitorLog is a Querydsl query type for VisitorLog
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVisitorLog extends EntityPathBase<VisitorLog> {

    private static final long serialVersionUID = -1934036944L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVisitorLog visitorLog = new QVisitorLog("visitorLog");

    public final StringPath applicationName = createString("applicationName");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DatePath<java.time.LocalDate> groupDate = createDate("groupDate", java.time.LocalDate.class);

    public final NumberPath<Long> hits = createNumber("hits", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final webbizz.crm.domain.member.entity.QMember member;

    public final StringPath remoteAddr = createString("remoteAddr");

    public final StringPath requestUri = createString("requestUri");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QVisitorLog(String variable) {
        this(VisitorLog.class, forVariable(variable), INITS);
    }

    public QVisitorLog(Path<? extends VisitorLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVisitorLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVisitorLog(PathMetadata metadata, PathInits inits) {
        this(VisitorLog.class, metadata, inits);
    }

    public QVisitorLog(Class<? extends VisitorLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new webbizz.crm.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

