package webbizz.crm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.AntPathMatcher;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.domain.member.enumset.MemberRole;
import webbizz.crm.service.member.MemberAdminService;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Collection;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberAdminService memberAdminService;

    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final SessionRegistry sessionRegistry;

    /**
     * Security Filter Chain 설정
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception 예외가 발생한 경우
     * @author TAEROK HWANG
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 인증 제공자 설정
        http.authenticationProvider(memberAdminService);

        // CSRF 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // HTTP Header 설정
        http.headers(header -> header
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                .httpStrictTransportSecurity(HeadersConfigurer.HstsConfig::disable)
        );

        // Method 에서 권한을 세부적으로 주기 위해 모든 요청을 허용
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .antMatchers("/css/**", "/images/**", "/js/**", "/webfonts/**").permitAll()
                .antMatchers("/api/v1/internal/**").permitAll()
                .antMatchers("/login", "/logout").permitAll()
                .anyRequest().authenticated());

        // 폼 로그인 설정
        http.formLogin(formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("loginId")
                .failureHandler(authenticationFailureHandler())
                .successHandler(authenticationSuccessHandler())
        );

        // 세션 설정
        http.sessionManagement(session -> session
                .maximumSessions(-1)
                .sessionRegistry(this.sessionRegistry)
                .expiredUrl("/login?expired")
        );

        // 로그아웃 설정
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
        );

        return http.build();
    }

    /**
     * 회원 등급 계층 설정 <br />
     * 상위 등급은 하위 등급의 모든 권한을 가진다. <br />
     * ex) ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER
     *
     * @return RoleHierarchy
     * @author TAEROK HWANG
     */
    @Bean
    static RoleHierarchy roleHierarchy() { // static
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(String.format("%s > %s", MemberRole.ADMIN.getCode(), MemberRole.USER.getCode()));
        return roleHierarchy;
    }

    /**
     * 커스텀 PermissionEvaluator 빈 등록
     *
     * @return PermissionEvaluator
     * @author TAEROK HWANG
     */
    @Bean
    static PermissionEvaluator permissionEvaluator() {
        return new CustomPermissionEvaluator();
    }

    /**
     * Method 에서 권한을 세부적으로 주기 위한 설정
     *
     * @return MethodSecurityExpressionHandler
     * @author TAEROK HWANG
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator());
        handler.setRoleHierarchy(roleHierarchy());
        return handler;
    }

    /**
     * 로그인 실패 핸들러
     *
     * @return AuthenticationFailureHandler
     * @author TAEROK HWANG
     */
    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            HttpSession session = request.getSession();

            session.setAttribute("errorMessage", exception.getMessage());
            response.sendRedirect("/login");
        };
    }

    /**
     * 로그인 성공 핸들러
     *
     * @return AuthenticationSuccessHandler
     * @author TAEROK HWANG
     */
    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 로그인 오류 메시지 제거
            HttpSession session = request.getSession();
            session.removeAttribute("errorMessage");

            // 로그인 전 페이지로 이동
            SavedRequest savedRequest = this.requestCache.getRequest(request, response);
            String targetUrl = (savedRequest != null)
                    ? savedRequest.getRedirectUrl()
                    : ((MemberUserDetails) authentication.getPrincipal()).getFirstMappingUrl(request, "/");

            response.sendRedirect(targetUrl);

            // 최근 로그인 일시 갱신
            memberAdminService.updateMemberLastLoginAt(authentication);
        };
    }

    /**
     * hasPermission(...) 파서 <br />
     * AntPattern 기반으로 권한을 확인한다. <br />
     * {@link Bean} 으로 등록되어야 Thymeleaf 에서 사용 가능
     *
     * @author TAEROK HWANG
     */
    public static class CustomPermissionEvaluator implements PermissionEvaluator {

        private final AntPathMatcher antPathMatcher = new AntPathMatcher();

        /**
         * Method 에서 사용하는 hasPermission(...) 동작 세부 설정
         *
         * @param authentication 현재 사용자의 권한 정보
         * @param targetDomainObject 권한을 확인해야 URL, 표현식이 AntPattern 으로 평가된다. (예: <code>/board/**</code>)
         * @param permission 권한 표현식. JAVA 코드 상에서는 사용하지 않음.
         *                   Thymeleaf 에서 2차 세부 메뉴 권한으로 줬을 때 1차 메뉴 링크를 표시하기 위해 사용. (LINK 전달)
         * @return boolean 권한 소유 여부. 권한이 없으면 false 반환 및 403 오류 발생
         */
        @Override
        public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            if (!(targetDomainObject instanceof String))
                return false;

            // 모든 권한을 가진 경우라면 항상 허용
            if (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("/**"))) {
                return true;
            }

            // LINK 로 권한을 확인하는 경우 (상위 -> 하위 권한 확인) 하위 권한을 하나라도 가지고 있다면 허용
            if ("LINK".equals(permission)) {
                return authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter(authority -> authority.startsWith("/"))
                        .anyMatch(authority -> {
                            String target = targetDomainObject.toString();
                            if (!target.endsWith("/**"))
                                target += "/**";

                            return antPathMatcher.matchStart(target, authority.replace("/**", ""));
                        });
            }

            // 일반 권한 확인
            return authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(authority -> authority.startsWith("/"))
                    .anyMatch(authority -> antPathMatcher.match(authority, targetDomainObject.toString()));
        }

        @Override
        public boolean hasPermission(Authentication authentication,
                                     Serializable targetId,
                                     String targetType,
                                     Object permission) {

            return this.hasPermission(authentication, targetId, permission);
        }

    }

}
