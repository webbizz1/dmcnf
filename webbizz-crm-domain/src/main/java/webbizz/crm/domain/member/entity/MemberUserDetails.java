package webbizz.crm.domain.member.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import webbizz.crm.domain.authoritypattern.entity.AuthorityPattern;
import webbizz.crm.domain.member.enumset.MemberStatus;
import webbizz.crm.domain.memberauthority.entity.MemberAuthority;
import webbizz.crm.util.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MemberUserDetails implements UserDetails {

    @Getter
    private final Member member;

    private final List<GrantedAuthority> authorities;

    public MemberUserDetails(Member member) {
        this.member = member;

        // ROLE_XXX 권한 설정
        this.authorities = AuthorityUtils.createAuthorityList(this.member.getRole().getCode());

        // 접근 가능한 URL 패턴 설정
        this.authorities.addAll(this.member.getAuthorities().stream()
                .map(MemberAuthority::getAuthorityPattern)
                .sorted(Comparator
                        .comparing(AuthorityPattern::getDepth)
                        .thenComparing(AuthorityPattern::getSortOrder))
                .map(AuthorityPattern::getUrlPattern)
                .map(AuthorityUtils::createAuthorityList)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableList(this.authorities);
    }

    @Override
    public String getPassword() {
        return this.member.getPassword();
    }

    @Override
    public String getUsername() {
        return this.member.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.member.getStatus() != MemberStatus.SUSPENDED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.member.getStatus() != MemberStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.member.getStatus() == MemberStatus.NORMAL && !this.member.getDelYn().isBool();
    }

    public boolean isAllowedIpAddress(String remoteAddr) {
        final List<String> allowedIps = this.member.getAllowIpAddresses();

        // 로컬 IP인 경우 로그인 허용
        if (RequestUtils.getLocalIpAddress().contains(remoteAddr))
            return true;

        // IP 제한이 없는 경우 로그인 허용
        if (allowedIps == null || allowedIps.isEmpty())
            return true;

        return allowedIps.contains(remoteAddr);
    }

    /**
     * 보유하고 있는 권한 중 요청한 URL 패턴과 일치하는 첫 번째 URL 을 반환
     *
     * @param request HttpServletRequest
     * @return 첫 번째로 일치하는 메뉴 URL
     * @author TAEROK HWANG
     */
    public String getFirstMappingUrl(HttpServletRequest request, String defaultUrl) {
        if (this.authorities.stream().anyMatch(authority -> authority.getAuthority().equals("/**")))
            return defaultUrl;

        return this.authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(pattern -> pattern.replace("/**", ""))
                .filter(pattern ->
                        !pattern.equals(request.getRequestURI()) && pattern.startsWith(request.getRequestURI()))
                .findFirst()
                .orElse(defaultUrl);
    }

}
