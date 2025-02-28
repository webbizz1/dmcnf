package webbizz.crm.domain.terms;

import webbizz.crm.domain.terms.dto.TermsCondition;
import webbizz.crm.domain.terms.dto.TermsDto;
import webbizz.crm.domain.terms.enumset.TermsType;

import java.util.List;
import java.util.Optional;

public interface TermsRepositoryCustom {

    /**
     * 약관 시퀀스와 약관 유형으로 약관 정보 조회
     *
     * @param id 약관 시퀀스
     * @param type 약관 유형
     * @return 약관 DTO (Optional)
     * @author TAEROK HWANG
     */
    Optional<TermsDto> searchBy(Long id, TermsType type);

    /**
     * 활성화된 약관 정보 조회
     *
     * @param type 약관 유형
     * @return 약관 DTO (Optional)
     * @author TAEROK HWANG
     */
    Optional<TermsDto> searchByForActive(TermsType type);

    /**
     * 검색 조건으로 약관 목록 조회
     *
     * @param condition 검색 조건
     * @return 약관 DTO 리스트
     * @author TAEROK HWANG
     */
    List<TermsDto> searchAllByCondition(TermsCondition condition);

}
