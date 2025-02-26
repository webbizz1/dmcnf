package webbizz.crm.domain.surveyresponsemember;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import webbizz.crm.domain.surveyresponsedetail.dto.SurveyResponseDetailDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyResponseMemberDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static webbizz.crm.domain.survey.QSurvey.survey;
import static webbizz.crm.domain.surveyquestionanswer.QSurveyQuestionAnswer.surveyQuestionAnswer;
import static webbizz.crm.domain.surveyresponsedetail.QSurveyResponseDetail.surveyResponseDetail;
import static webbizz.crm.domain.surveyresponsemember.QSurveyResponseMember.surveyResponseMember;
import static webbizz.crm.util.QuerydslUtils.condEq;
import static webbizz.crm.util.QuerydslUtils.condIn;

@Repository
@RequiredArgsConstructor
public class SurveyResponseMemberRepositoryImpl implements SurveyResponseMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SurveyResponseMemberDto> searchAllSurveyResponseBySurveyId(final Long surveyId,
                                                                           final Pageable pageable
    ) {
        final List<SurveyResponseMemberDto> content = queryFactory
                .select(Projections.fields(SurveyResponseMemberDto.class,
                        surveyResponseMember.id,
                        surveyResponseMember.survey.id.as("surveyId"),
                        surveyResponseMember.respondentName,
                        surveyResponseMember.respondentPhone,
                        surveyResponseMember.respondentEmail,
                        surveyResponseMember.overallComment,
                        surveyResponseMember.createdAt
                ))
                .from(surveyResponseMember)
                .innerJoin(surveyResponseMember.survey, survey)
                .where(condEq(surveyResponseMember.survey.id, surveyId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(surveyResponseMember.id.desc())
                .fetch();

        // 응답자 아이디 목록
        final List<Long> responseMemberIds = content.stream()
                .map(SurveyResponseMemberDto::getId)
                .collect(Collectors.toList());

        // 설문 응답 상세 정보 조회
        final List<SurveyResponseDetailDto> details = this.findSurveyResponseDetailByMemberIds(responseMemberIds);

        // 응답 상세 정보를 중첩 Map 구조로 변환
        final Map<Long, Map<Long, String>> responseMap = details.stream()
                .collect(Collectors.groupingBy(
                        SurveyResponseDetailDto::getResponseMemberId,
                        Collectors.groupingBy(
                                SurveyResponseDetailDto::getQuestionId,
                                Collectors.mapping(
                                        detail -> detail.getAnswerId() != null ?
                                                String.valueOf(detail.getSortOrder()) : "기타",
                                        Collectors.joining(",")
                                )
                        )
                ));

        // 각 응답자의 해당하는 문항에대한 답을 셋팅
        content.forEach(memberDto -> {
            final Map<Long, Map<Long, String>> memberQuestions = new HashMap<>();
            final Map<Long, String> questionAnswers = responseMap.get(memberDto.getId());
            if (questionAnswers != null) {
                memberQuestions.put(memberDto.getId(), questionAnswers);
            }
            memberDto.setQuestions(memberQuestions);
        });

        final JPAQuery<Long> countQuery = queryFactory.select(Wildcard.count)
                .from(surveyResponseMember)
                .innerJoin(surveyResponseMember.survey, survey)
                .where(condEq(surveyResponseMember.survey.id, surveyId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<SurveyResponseDetailDto> findSurveyResponseDetailByMemberIds(final List<Long> responseMemberIds) {
        return queryFactory
                .select(Projections.fields(SurveyResponseDetailDto.class,
                        surveyResponseDetail.surveyResponseMember.id.as("responseMemberId"),
                        surveyResponseDetail.surveyQuestion.id.as("questionId"),
                        surveyQuestionAnswer.id.as("answerId"),
                        surveyQuestionAnswer.sortOrder.as("sortOrder"),
                        surveyResponseDetail.customAnswerText.as("answerText")
                ))
                .from(surveyResponseDetail)
                .innerJoin(surveyResponseDetail.surveyResponseMember, surveyResponseMember)
                .leftJoin(surveyResponseDetail.surveyQuestionAnswer, surveyQuestionAnswer)
                .where(condIn(surveyResponseDetail.surveyResponseMember.id, responseMemberIds))
                .fetch();
    }

}
