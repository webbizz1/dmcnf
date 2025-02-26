package webbizz.crm.domain.surveyquestionanswer;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSurveyQuestionAnswer is a Querydsl query type for SurveyQuestionAnswer
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSurveyQuestionAnswer extends EntityPathBase<SurveyQuestionAnswer> {

    private static final long serialVersionUID = 1926836903L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSurveyQuestionAnswer surveyQuestionAnswer = new QSurveyQuestionAnswer("surveyQuestionAnswer");

    public final webbizz.crm.domain.QUpdatedBaseEntity _super = new webbizz.crm.domain.QUpdatedBaseEntity(this);

    public final StringPath answerText = createString("answerText");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    public final EnumPath<webbizz.crm.domain.YN> customYn = createEnum("customYn", webbizz.crm.domain.YN.class);

    //inherited
    public final EnumPath<webbizz.crm.domain.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final webbizz.crm.domain.surveyquestion.QSurveyQuestion surveyQuestion;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSurveyQuestionAnswer(String variable) {
        this(SurveyQuestionAnswer.class, forVariable(variable), INITS);
    }

    public QSurveyQuestionAnswer(Path<? extends SurveyQuestionAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSurveyQuestionAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSurveyQuestionAnswer(PathMetadata metadata, PathInits inits) {
        this(SurveyQuestionAnswer.class, metadata, inits);
    }

    public QSurveyQuestionAnswer(Class<? extends SurveyQuestionAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.surveyQuestion = inits.isInitialized("surveyQuestion") ? new webbizz.crm.domain.surveyquestion.QSurveyQuestion(forProperty("surveyQuestion"), inits.get("surveyQuestion")) : null;
    }

}

