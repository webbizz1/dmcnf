package webbizz.crm.domain.memberauthority;

import java.util.List;

public interface MemberAuthorityRepositoryCustom {

    /**
     * 회원 시퀀스로 회원 권한 시퀀스 조회
     *
     * @param memberId 회원 시퀀스
     * @return 회원 권한 시퀀스 리스트
     * @author TAEROK HWANG
     */
    List<Long> searchPatternIdsByMemberId(Long memberId);

    /**
     * 회원 메뉴 권한 정보 삭제 (hard delete)
     *
     * @param memberId 회원 시퀀스
     * @author TAEROK HWANG
     */
    void deleteByMemberId(Long memberId);

}
