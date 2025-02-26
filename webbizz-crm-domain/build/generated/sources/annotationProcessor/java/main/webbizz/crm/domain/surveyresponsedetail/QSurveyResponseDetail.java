package webbizz.crm.domain.surveyresponsedetail;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSurveyResponseDetail is a Querydsl query type for SurveyResponseDetail
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSurveyResponseDetail extends EntityPathBase<SurveyResponseDetail> {

    private static final long serialVersionUID = 559287143L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSurveyResponseDetail surveyResponseDetail = new QSurveyResponseDetail("surveyResponseDetail");

    public final webbizz.crm.domain.QUpdatedBaseEntity _super = new webbizz.crm.domain.QUpdatedBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    public final StringPath customAnswerText = createString("customAnswerText");

    //inherited
    public final EnumPath<webbizz.crm.domain.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final webbizz.crm.domain.surveyquestion.QSurveyQuestion surveyQuestion;

    public final webbizz.crm.domain.surveyquestionanswer.QSurveyQuestionAnswer surveyQuestionAnswer;

    public final webbizz.crm.domain.surveyresponsemember.QSurveyResponseMember surveyResponseMember;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSurveyResponseDetail(String variable) {
        this(SurveyResponseDetail.class, forVariable(variable), INITS);
    }

    public QSurveyResponseDetail(Path<? extends SurveyResponseDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSurveyResponseDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSurveyResponseDetail(PathMetadata metadata, PathInits inits) {
        this(SurveyResponseDetail.class, metadata, inits);
    }

    public QSurveyResponseDetail(Class<? extends SurveyResponseDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.surveyQuestion = inits.isInitialized("surveyQuestion") ? new webbizz.crm.domain.surveyquestion.QSurveyQuestion(forProperty("surveyQuestion"), inits.get("surveyQuestion")) : null;
        this.surveyQuestionAnswer = inits.isInitialized("surveyQuestionAnswer") ? new webbizz.crm.domain.surveyquestionanswer.QSurveyQuestionAnswer(forProperty("surveyQuestionAnswer"), inits.get("surveyQuestionAnswer")) : null;
        this.surveyResponseMember = inits.isInitialized("surveyResponseMember") ? new webbizz.crm.domain.surveyresponsemember.QSurveyResponseMember(forProperty("surveyResponseMember"), inits.get("surveyResponseMember")) : null;
    }

}

