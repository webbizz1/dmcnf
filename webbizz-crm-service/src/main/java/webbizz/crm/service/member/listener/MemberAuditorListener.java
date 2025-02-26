package webbizz.crm.service.member.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import webbizz.crm.domain.member.entity.Member;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.domain.memberauditor.MemberAuditorRepository;
import webbizz.crm.domain.memberauditor.entity.MemberAuditor;
import webbizz.crm.service.member.dto.MemberAuditorEvent;
import webbizz.crm.util.RequestUtils;

@Component
@RequiredArgsConstructor
public class MemberAuditorListener {

    private final MemberAuditorRepository memberAuditorRepository;

    @EventListener
    public void handleMemberLoginEvent(MemberAuditorEvent memberAuditorEvent) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof MemberUserDetails) {
            Member member = ((MemberUserDetails) authentication.getPrincipal()).getMember();

            MemberAuditor memberAuditor = MemberAuditor.builder()
                    .member(member)
                    .targetMember(memberAuditorEvent.getMember())
                    .type(memberAuditorEvent.getType())
                    .remoteAddr(RequestUtils.getRemoteAddr())
                    .build();

            memberAuditorRepository.save(memberAuditor);
        }
    }

}
