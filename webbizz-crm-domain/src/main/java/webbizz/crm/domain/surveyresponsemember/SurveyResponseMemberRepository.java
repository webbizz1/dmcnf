package webbizz.crm.domain.surveyresponsemember;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyResponseMemberRepository extends JpaRepository<SurveyResponseMember, Long>,
        SurveyResponseMemberRepositoryCustom {

    /**
     * 특정 설문에 대한 사용자가 응답을 했는지 안했는지 확인
     *
     * @param surveyId 응답 데이터를 조회할 설문의 ID
     * @return 응답 데이터가 존재하면 {@code true}, 없으면 {@code false}
     * @author YONGHO LEE
     */
    boolean existsBySurveyId(final Long surveyId);
}
