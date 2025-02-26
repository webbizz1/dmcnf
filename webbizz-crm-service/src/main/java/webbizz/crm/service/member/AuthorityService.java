package webbizz.crm.service.member;

import webbizz.crm.domain.authoritypattern.dto.AuthorityPatternDto;
import webbizz.crm.domain.member.enumset.MemberRole;

import java.util.List;

public interface AuthorityService {

    /**
     * 권한과 일치하고 관리되는 모든 권한 패턴 조회
     *
     * @param role 권한
     * @return 권한 패턴 DTO 리스트
     * @author TAEROK HWANG
     */
    List<AuthorityPatternDto> searchAllByRole(MemberRole role);

}
