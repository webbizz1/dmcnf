package webbizz.crm.service.member;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import webbizz.crm.domain.member.MemberRepository;
import webbizz.crm.domain.member.entity.Member;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.domain.member.enumset.MemberRole;
import webbizz.crm.util.RequestUtils;

import java.time.LocalDateTime;

public abstract class AbstractMemberServiceImpl extends EgovAbstractServiceImpl implements AbstractMemberService {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // ↑ 아이디 없으면 UsernameNotFoundException 예외 발생
        MemberUserDetails memberUserDetails = (MemberUserDetails) this.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, memberUserDetails.getPassword()))
            throw new BadCredentialsException("로그인 정보가 일치하지 않습니다.");

        if (!memberUserDetails.isAccountNonExpired())
            throw new AccountExpiredException("정지된 계정입니다. 관리자에게 문의하세요.");

        if (!memberUserDetails.isAccountNonLocked())
            throw new LockedException("잠긴 계정입니다. 관리자에게 문의하세요.");

        if (!memberUserDetails.isCredentialsNonExpired())
            throw new CredentialsExpiredException("만료된 계정입니다. 관리자에게 문의하세요.");

        if (!memberUserDetails.isEnabled())
            throw new DisabledException("정상 계정이 아닙니다. 관리자에게 문의하세요.");

        if (!memberUserDetails.isAllowedIpAddress(RequestUtils.getRemoteAddr())) {
            throw new InsufficientAuthenticationException(
                    String.format("허용되지 않은 IP에서 로그인이 불가능합니다: '%s'", RequestUtils.getRemoteAddr()));
        }

        return new UsernamePasswordAuthenticationToken(
                memberUserDetails,
                memberUserDetails.getPassword(),
                memberUserDetails.getAuthorities());
    }

    @Override
    @Transactional(readOnly = true)
    public String checkLoginId(final String loginId, final MemberRole role) {
        if (memberRepository.countByLoginId(loginId, role) > 0) {
            return String.format("이미 존재하는 아이디입니다: '%s'", loginId);
        }

        return null;
    }

    @Override
    public void updateMemberLastLoginAt(final Authentication authentication) {
        if (authentication.getPrincipal() instanceof MemberUserDetails) {
            Member member = ((MemberUserDetails) authentication.getPrincipal()).getMember();
            member.setLastLoginAt(LocalDateTime.now());
            memberRepository.save(member);
        }
    }

}
