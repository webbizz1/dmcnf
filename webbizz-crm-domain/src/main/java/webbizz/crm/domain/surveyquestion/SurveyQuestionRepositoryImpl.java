package webbizz.crm.domain.surveyquestion;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.surveyquestion.dto.SurveyQuestionDto;
import webbizz.crm.domain.surveyquestionanswer.dto.SurveyQuestionAnswerDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static webbizz.crm.domain.surveyquestion.QSurveyQuestion.surveyQuestion;
import static webbizz.crm.domain.surveyquestionanswer.QSurveyQuestionAnswer.surveyQuestionAnswer;
import static webbizz.crm.util.QuerydslUtils.condEq;


@Repository
@RequiredArgsConstructor
public class SurveyQuestionRepositoryImpl implements SurveyQuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<SurveyQuestionDto>> findAllSurveyQuestionBySurveyId(final Long surveyId) {
        // 1. 설문조사 질문 조회
        final List<SurveyQuestionDto> surveyQuestions = queryFactory
                .select(Projections.fields(
                        SurveyQuestionDto.class,
                        surveyQuestion.id,
                        surveyQuestion.title,
                        surveyQuestion.answerType,
                        surveyQuestion.sortOrder,
                        surveyQuestion.useCustomAnswerYn
                ))
                .from(surveyQuestion)
                .where(condEq(surveyQuestion.survey.id, surveyId))
                .orderBy(surveyQuestion.sortOrder.asc())
                .fetch();

        if (surveyQuestions.isEmpty()) {
            return Optional.empty();
        }

        // 2. 모든 질문의 대한 질문의 답변들 조회
        List<SurveyQuestionAnswerDto> allAnswers = queryFactory
                .select(Projections.constructor(
                        SurveyQuestionAnswerDto.class,
                        surveyQuestionAnswer.id.as("answerId"),
                        surveyQuestionAnswer.surveyQuestion.id.as("surveyQuestionId"),
                        surveyQuestionAnswer.answerText.as("text"),
                        surveyQuestionAnswer.sortOrder
                ))
                .from(surveyQuestionAnswer)
                .where(surveyQuestionAnswer.surveyQuestion.id.in(
                        surveyQuestions.stream().map(SurveyQuestionDto::getId).collect(Collectors.toList())
                ))
                .orderBy(surveyQuestionAnswer.sortOrder.asc())
                .fetch();

        // 3. 질문의 ID 별로 답변 그룹화
        final Map<Long, List<SurveyQuestionAnswerDto>> answerMap = allAnswers.stream()
                .collect(Collectors.groupingBy(SurveyQuestionAnswerDto::getSurveyQuestionId));

        // 4. 해당 질문에 대한 답변 할당
        surveyQuestions.forEach(question ->
                question.setAnswers(answerMap.getOrDefault(question.getId(), new ArrayList<>())));

        return Optional.of(surveyQuestions);
    }

    @Override
    public List<Long> findQuestionIdsBySurveyId(final Long surveyId) {
        return new ArrayList<>(queryFactory
                .select(surveyQuestion.id)
                .from(surveyQuestion)
                .where(condEq(surveyQuestion.survey.id, surveyId), condEq(surveyQuestion.delYn, YN.N))
                .orderBy(surveyQuestion.sortOrder.asc()) // 노출순서 오름차순 정렬
                .fetch());
    }

}
