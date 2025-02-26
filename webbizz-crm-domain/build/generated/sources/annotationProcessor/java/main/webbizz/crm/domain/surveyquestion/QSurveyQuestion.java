package webbizz.crm.domain.surveyquestion;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSurveyQuestion is a Querydsl query type for SurveyQuestion
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSurveyQuestion extends EntityPathBase<SurveyQuestion> {

    private static final long serialVersionUID = 508599239L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSurveyQuestion surveyQuestion = new QSurveyQuestion("surveyQuestion");

    public final webbizz.crm.domain.QUpdatedBaseEntity _super = new webbizz.crm.domain.QUpdatedBaseEntity(this);

    public final EnumPath<webbizz.crm.domain.survey.enumset.AnswerType> answerType = createEnum("answerType", webbizz.crm.domain.survey.enumset.AnswerType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<webbizz.crm.domain.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final webbizz.crm.domain.survey.QSurvey survey;

    public final ListPath<webbizz.crm.domain.surveyquestionanswer.SurveyQuestionAnswer, webbizz.crm.domain.surveyquestionanswer.QSurveyQuestionAnswer> surveyQuestionAnswer = this.<webbizz.crm.domain.surveyquestionanswer.SurveyQuestionAnswer, webbizz.crm.domain.surveyquestionanswer.QSurveyQuestionAnswer>createList("surveyQuestionAnswer", webbizz.crm.domain.surveyquestionanswer.SurveyQuestionAnswer.class, webbizz.crm.domain.surveyquestionanswer.QSurveyQuestionAnswer.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final EnumPath<webbizz.crm.domain.YN> useCustomAnswerYn = createEnum("useCustomAnswerYn", webbizz.crm.domain.YN.class);

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSurveyQuestion(String variable) {
        this(SurveyQuestion.class, forVariable(variable), INITS);
    }

    public QSurveyQuestion(Path<? extends SurveyQuestion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSurveyQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSurveyQuestion(PathMetadata metadata, PathInits inits) {
        this(SurveyQuestion.class, metadata, inits);
    }

    public QSurveyQuestion(Class<? extends SurveyQuestion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.survey = inits.isInitialized("survey") ? new webbizz.crm.domain.survey.QSurvey(forProperty("survey")) : null;
    }

}

