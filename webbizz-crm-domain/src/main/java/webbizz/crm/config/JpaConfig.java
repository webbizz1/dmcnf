package webbizz.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import webbizz.crm.domain.member.entity.Member;
import webbizz.crm.domain.member.entity.MemberUserDetails;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    /**
     * JPA Auditing(등록자, 수정자 자동 삽입)을 위한 Bean 등록 <br />
     * {@link EnableJpaAuditing} annotation 필요
     *
     * @return AuditorAware<String>
     * @author TAEROK HWANG
     */
    @Bean
    public AuditorAware<String> auditProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String auditorId = "anonymousUser";

            if (authentication != null && authentication.getPrincipal() instanceof MemberUserDetails) {
                Member member = ((MemberUserDetails) authentication.getPrincipal()).getMember();
                auditorId = String.format("%d:%s", member.getId(), member.getLoginId());
            }

            return Optional.of(auditorId);
        };
    }

}
