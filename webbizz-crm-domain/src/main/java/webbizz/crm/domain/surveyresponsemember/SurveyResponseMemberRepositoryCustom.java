package webbizz.crm.domain.surveyresponsemember;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import webbizz.crm.domain.surveyresponsedetail.dto.SurveyResponseDetailDto;
import webbizz.crm.domain.surveyresponsemember.dto.SurveyResponseMemberDto;

import java.util.List;

public interface SurveyResponseMemberRepositoryCustom {

    /**
     * 콘텐츠 관리 - 민원 만족도 조사 - 결과 보기 (page)
     *
     * @param id       설문 응답 참여자 정보 시퀀스 목록
     * @param pageable 페이징 족건
     * @return Page<SurveyResponseMemberDto> 해당 설문의 응답한 회원들의 리스트(Page)
     * @author YONGHO LEE
     */
    Page<SurveyResponseMemberDto> searchAllSurveyResponseBySurveyId(final Long id, final Pageable pageable);

    /**
     * 해당설문의 응답한 것들을 조회
     *
     * @param responseMemberIds 설문 응답 참여자 정보 시퀀스 목록
     * @return List<SurveyResponseDetailDto> 응답 상세 DTO
     * @author YONGHO LEE
     */
    List<SurveyResponseDetailDto> findSurveyResponseDetailByMemberIds(final List<Long> responseMemberIds);
}
