package webbizz.crm.domain.surveyresponsemember;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSurveyResponseMember is a Querydsl query type for SurveyResponseMember
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSurveyResponseMember extends EntityPathBase<SurveyResponseMember> {

    private static final long serialVersionUID = -1852603641L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSurveyResponseMember surveyResponseMember = new QSurveyResponseMember("surveyResponseMember");

    public final webbizz.crm.domain.QUpdatedBaseEntity _super = new webbizz.crm.domain.QUpdatedBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<webbizz.crm.domain.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath overallComment = createString("overallComment");

    public final StringPath remoteAddr = createString("remoteAddr");

    public final StringPath respondentEmail = createString("respondentEmail");

    public final StringPath respondentName = createString("respondentName");

    public final StringPath respondentPhone = createString("respondentPhone");

    public final DateTimePath<java.time.LocalDateTime> responseDate = createDateTime("responseDate", java.time.LocalDateTime.class);

    public final ListPath<webbizz.crm.domain.surveyresponsedetail.SurveyResponseDetail, webbizz.crm.domain.surveyresponsedetail.QSurveyResponseDetail> responses = this.<webbizz.crm.domain.surveyresponsedetail.SurveyResponseDetail, webbizz.crm.domain.surveyresponsedetail.QSurveyResponseDetail>createList("responses", webbizz.crm.domain.surveyresponsedetail.SurveyResponseDetail.class, webbizz.crm.domain.surveyresponsedetail.QSurveyResponseDetail.class, PathInits.DIRECT2);

    public final webbizz.crm.domain.survey.QSurvey survey;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSurveyResponseMember(String variable) {
        this(SurveyResponseMember.class, forVariable(variable), INITS);
    }

    public QSurveyResponseMember(Path<? extends SurveyResponseMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSurveyResponseMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSurveyResponseMember(PathMetadata metadata, PathInits inits) {
        this(SurveyResponseMember.class, metadata, inits);
    }

    public QSurveyResponseMember(Class<? extends SurveyResponseMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.survey = inits.isInitialized("survey") ? new webbizz.crm.domain.survey.QSurvey(forProperty("survey")) : null;
    }

}

