package webbizz.crm.service.member;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import webbizz.crm.domain.member.dto.MemberAdminDto;
import webbizz.crm.domain.member.dto.MemberAdminSaveDto;
import webbizz.crm.domain.member.dto.MemberCondition;

public interface MemberAdminService extends AbstractMemberService {

    /**
     * 회원 시퀀스로 관리자 회원 정보 조회
     *
     * @param id 회원 시퀀스
     * @return 관리자 회원 DTO
     * @author TAEROK HWANG
     */
    MemberAdminDto searchMemberAdminById(Long id);

    /**
     * 검색 조건으로 관리자 회원 목록 조회
     *
     * @param condition 검색 조건
     * @return 관리자 회원 DTO 리스트 (Page)
     * @author TAEROK HWANG
     */
    Page<MemberAdminDto> searchAllMemberAdminByCondition(MemberCondition condition);

    /**
     * 관리자 회원 등록·수정
     *
     * @param requestDto 관리자 회원 저장 요청 DTO
     * @param httpMethod HTTP 메소드
     * @return 회원 시퀀스
     * @author TAEROK HWANG
     */
    Long saveMemberAdmin(MemberAdminSaveDto requestDto, HttpMethod httpMethod);

}
