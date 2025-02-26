package webbizz.crm.service.terms;

import org.springframework.http.HttpMethod;
import webbizz.crm.domain.terms.dto.TermsCondition;
import webbizz.crm.domain.terms.dto.TermsDto;
import webbizz.crm.domain.terms.dto.TermsSaveDto;
import webbizz.crm.domain.terms.enumset.TermsType;

import java.util.List;

public interface TermsService {

    /**
     * 약관 시퀀스와 약관 유형으로 약관 정보 조회
     *
     * @param id 약관 시퀀스
     * @param type 약관 유형
     * @return 약관 DTO (Optional)
     * @author TAEROK HWANG
     */
    TermsDto searchBy(Long id, TermsType type);

    /**
     * 활성화된 약관 정보 조회
     *
     * @param type 약관 유형
     * @return 약관 DTO (Optional)
     * @author TAEROK HWANG
     */
    TermsDto searchByForActive(TermsType type);

    /**
     * 검색 조건으로 약관 목록 조회
     *
     * @param condition 검색 조건
     * @return 약관 DTO 리스트
     * @author TAEROK HWANG
     */
    List<TermsDto> searchAllByCondition(TermsCondition condition);

    /**
     * 약관 정보 등록·수정
     *
     * @param requestDto 약관 정보 저장 요청 DTO
     * @param httpMethod HTTP 메소드
     * @return 약관 시퀀스
     */
    Long saveTerms(TermsSaveDto requestDto, HttpMethod httpMethod);

    /**
     * 약관 활성화
     *
     * @param id 약관 시퀀스
     * @author TAEROK HWANG
     */
    void activeTerms(Long id);

}
