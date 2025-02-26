package webbizz.crm.domain.survey;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSurvey is a Querydsl query type for Survey
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSurvey extends EntityPathBase<Survey> {

    private static final long serialVersionUID = 1124746855L;

    public static final QSurvey survey = new QSurvey("survey");

    public final webbizz.crm.domain.QUpdatedBaseEntity _super = new webbizz.crm.domain.QUpdatedBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<webbizz.crm.domain.YN> delYn = _super.delYn;

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final ListPath<webbizz.crm.domain.surveyquestion.SurveyQuestion, webbizz.crm.domain.surveyquestion.QSurveyQuestion> surveyQuestions = this.<webbizz.crm.domain.surveyquestion.SurveyQuestion, webbizz.crm.domain.surveyquestion.QSurveyQuestion>createList("surveyQuestions", webbizz.crm.domain.surveyquestion.SurveyQuestion.class, webbizz.crm.domain.surveyquestion.QSurveyQuestion.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public final EnumPath<webbizz.crm.domain.YN> viewYn = createEnum("viewYn", webbizz.crm.domain.YN.class);

    public QSurvey(String variable) {
        super(Survey.class, forVariable(variable));
    }

    public QSurvey(Path<? extends Survey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSurvey(PathMetadata metadata) {
        super(Survey.class, metadata);
    }

}

