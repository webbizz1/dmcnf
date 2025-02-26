package webbizz.crm.service.member;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import webbizz.crm.domain.member.enumset.MemberRole;

public interface AbstractMemberService extends UserDetailsService, AuthenticationProvider {

    /**
     * 아이디 유효성 확인
     *
     * @param loginId 아이디
     * @param role 회원 권한
     * @return 중복 여부 (NULL: 중복 없음, 그 외: 오류 메시지)
     * @author TAEROK HWANG
     */
    String checkLoginId(String loginId, MemberRole role);

    /**
     * 회원 로그인 일시 업데이트
     *
     * @param authentication 인증 정보
     * @author TAEROK HWANG
     */
    void updateMemberLastLoginAt(Authentication authentication);

    default boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
