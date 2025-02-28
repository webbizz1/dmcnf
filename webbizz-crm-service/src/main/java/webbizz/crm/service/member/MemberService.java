package webbizz.crm.service.member;

import org.springframework.data.domain.Page;
import webbizz.crm.domain.member.dto.MemberCondition;
import webbizz.crm.domain.member.dto.MemberDto;
import webbizz.crm.domain.member.dto.MemberPasswordResetDto;
import webbizz.crm.service.excel.ExcelFile;

public interface MemberService extends AbstractMemberService {

    /**
     * 회원 시퀀스로 회원 정보 조회
     *
     * @param id 회원 시퀀스
     * @return 회원 DTO
     * @author TAEROK HWANG
     */
    MemberDto searchMemberById(Long id);

    /**
     * 회원 시퀀스로 탈퇴 회원 정보 조회
     *
     * @param id 회원 시퀀스
     * @return 회원 DTO
     * @author TAEROK HWANG
     */
    MemberDto searchWithdrawalMemberById(Long id);

    /**
     * 검색 조건으로 회원 목록 조회
     *
     * @param condition 검색 조건
     * @return 회원 DTO 리스트 (Page)
     * @author TAEROK HWANG
     */
    Page<MemberDto> searchAllMemberByCondition(MemberCondition condition);

    /**
     * 검색 조건으로 탈퇴 회원 목록 조회
     *
     * @param condition 검색 조건
     * @return 회원 DTO 리스트 (Page)
     * @author TAEROK HWANG
     */
    Page<MemberDto> searchAllWithdrawalMemberByCondition(MemberCondition condition);

    /**
     * 활성 회원 수 조회
     *
     * @return 활성 회원 수 (Long)
     * @author TAEROK HWANG
     */
    Long countMemberForActive();

    /**
     * 회원 비밀번호 초기화
     *
     * @param requestDto 회원 비밀번호 초기화 DTO
     * @return 초기화된 비밀번호
     * @author TAEROK HWANG
     */
    String resetPassword(MemberPasswordResetDto requestDto);

    /**
     * 활동 회원 목록 엑셀 다운로드
     *
     * @param condition 검색 조건
     * @return 엑셀 파일 객체
     * @author TAEROK HWANG
     */
    ExcelFile<?> searchMemberForExcel(MemberCondition condition);

    /**
     * 탈퇴 회원 목록 엑셀 다운로드
     *
     * @param condition 검색 조건
     * @return 엑셀 파일 객체
     * @author TAEROK HWANG
     */
    ExcelFile<?> searchWithdrawalMemberForExcel(MemberCondition condition);

}
