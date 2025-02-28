package webbizz.crm.domain.member;

import org.springframework.data.domain.Page;
import webbizz.crm.domain.member.dto.MemberAdminDto;
import webbizz.crm.domain.member.dto.MemberCondition;
import webbizz.crm.domain.member.dto.MemberDto;

import java.util.Optional;

public interface MemberRepositoryCustom {

    /**
     * 회원 시퀀스로 회원 정보 조회
     *
     * @param id 회원 시퀀스
     * @return 관리자 회원 DTO (Optional)
     * @author TAEROK HWANG
     */
    Optional<MemberDto> searchMemberById(Long id);

    /**
     * 검색 조건으로 회원 목록 조회
     *
     * @param condition 검색 조건
     * @return 관리자 회원 DTO 리스트 (Page)
     * @author TAEROK HWANG
     */
    Page<MemberDto> searchAllMemberByCondition(MemberCondition condition);

    /**
     * 활성 회원 수 조회
     *
     * @return 활성 회원 수 (Long)
     * @author TAEROK HWANG
     */
    Long countMemberForActive();

    /**
     * 회원 시퀀스로 관리자 회원 정보 조회
     *
     * @param id 회원 시퀀스
     * @return 관리자 회원 DTO (Optional)
     * @author TAEROK HWANG
     */
    Optional<MemberAdminDto> searchMemberAdminById(Long id);

    /**
     * 검색 조건으로 관리자 회원 목록 조회
     *
     * @param condition 검색 조건
     * @return 관리자 회원 DTO 리스트 (Page)
     * @author TAEROK HWANG
     */
    Page<MemberAdminDto> searchAllMemberAdminByCondition(MemberCondition condition);

}
