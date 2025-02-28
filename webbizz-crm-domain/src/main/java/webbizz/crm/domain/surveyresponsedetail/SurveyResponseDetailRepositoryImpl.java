package webbizz.crm.domain.surveyresponsedetail;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import webbizz.crm.domain.surveyresponsedetail.dto.SurveyAnswerStatisticsDto;
import webbizz.crm.domain.surveyresponsedetail.dto.SurveyResponseDetailAnswerDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyOverallCommentDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SurveyResponseDetailRepositoryImpl implements SurveyResponseDetailRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<Long, Map<Long, SurveyAnswerStatisticsDto>> getSurveyStatistics(final Long surveyId) {
        final String sql = this.sqlStringTemplate();
        final Map<Long, Map<Long, SurveyAnswerStatisticsDto>> result = new HashMap<>();

        jdbcTemplate.query(sql, (rs) -> {
            final Long questionId = rs.getLong("survey_question_id");
            final Long answerId = rs.getLong("survey_question_answer_id");
            final Long answerCount = rs.getLong("answer_count");
            final Long totalResponses = rs.getLong("total_responses");
            final String customAnswer = rs.getString("custom_answer_text");
            final String email = rs.getString("respondent_email");

            // 질문별 Map 없으면 생성
            result.putIfAbsent(questionId, new HashMap<>());
            final Map<Long, SurveyAnswerStatisticsDto> answerMap = result.get(questionId);

            // 답변 통계 생성 또는 업데이트
            final SurveyAnswerStatisticsDto statistics = answerMap.computeIfAbsent(
                    answerId,
                    k -> SurveyAnswerStatisticsDto.createStatistics(
                            questionId,
                            answerId,
                            answerCount,
                            totalResponses
                    )
            );

            // 기타응답이 있는 경우 추가
            if (customAnswer != null && email != null) {
                statistics.getCustomAnswers().add(
                        new SurveyResponseDetailAnswerDto(questionId, email, customAnswer)
                );
            }

        }, surveyId, surveyId, surveyId, surveyId);

        return result;
    }

    @Override
    public List<SurveyOverallCommentDto> findOverallCommentsBySurveyId(final Long surveyId) {
        final String sql =
                "SELECT " +
                "    (SELECT COUNT(*) FROM survey_response_member WHERE survey_id = ? AND del_yn = 'N')" +
                "                                                                        as total_count, " +
                "    respondent_email as email, " +
                "    overall_comment " +
                "FROM survey_response_member " +
                "WHERE survey_id = ? " +
                "AND del_yn = 'N' " +
                "ORDER BY created_at DESC";

        return jdbcTemplate.query(
                sql, (rs, rowNum) -> new SurveyOverallCommentDto(
                        rs.getLong("total_count"),
                        rs.getString("email"),
                        rs.getString("overall_comment")
                ), surveyId, surveyId);
    }

    /**
     * SQL 쿼리에서 사용되는 매개변수 명칭은 데이터베이스 테이블의 컬럼 명칭(뱀표기법)과 일치시키는 것
     */
    private String sqlStringTemplate() {
        return
                "WITH total_responses AS ( " +
                "    SELECT COUNT(DISTINCT rm_total.survey_response_member_id) as total " +
                "    FROM survey_response_member rm_total " +
                "    WHERE rm_total.survey_id = ? AND rm_total.del_yn = 'N' " +
                ") " +
                "SELECT " +
                "    q.survey_question_id, " +
                "    qa.survey_question_answer_id, " +
                "    qa.answer_text, " +
                "    COUNT(DISTINCT rd.survey_response_member_id) as answer_count, " +
                "    tr.total as total_responses, " +
                "    rd2.custom_answer_text, " +
                "    rm2.respondent_email " +
                "FROM survey_question q " +
                "JOIN survey_question_answer qa ON q.survey_question_id = qa.survey_question_id " +
                "CROSS JOIN total_responses tr " +
                "LEFT JOIN survey_response_detail rd " +
                "    ON rd.survey_question_id = q.survey_question_id " +
                "    AND rd.survey_question_answer_id = qa.survey_question_answer_id " +
                "    AND rd.del_yn = 'N' " +
                "LEFT JOIN survey_response_member rm " +
                "    ON rd.survey_response_member_id = rm.survey_response_member_id " +
                "    AND rm.survey_id = ? " +
                "    AND rm.del_yn = 'N' " +
                "LEFT JOIN survey_response_detail rd2 " +
                "    ON rd2.survey_question_id = q.survey_question_id " +
                "    AND rd2.custom_answer_text IS NOT NULL " +
                "    AND rd2.del_yn = 'N' " +
                "LEFT JOIN survey_response_member rm2 " +
                "    ON rd2.survey_response_member_id = rm2.survey_response_member_id " +
                "    AND rm2.survey_id = ? " +
                "    AND rm2.del_yn = 'N' " +
                "WHERE q.survey_id = ? " +
                "AND q.del_yn = 'N' " +
                "AND qa.del_yn = 'N' " +
                "GROUP BY " +
                "    q.survey_question_id, " +
                "    qa.survey_question_answer_id, " +
                "    qa.answer_text, " +
                "    tr.total, " +
                "    rd2.custom_answer_text, " +
                "    rm2.respondent_email " +
                "ORDER BY q.sort_order, qa.sort_order";
    }

}