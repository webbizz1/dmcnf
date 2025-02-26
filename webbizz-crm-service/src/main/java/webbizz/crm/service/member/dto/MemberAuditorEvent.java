package webbizz.crm.service.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.domain.member.entity.Member;
import webbizz.crm.domain.member.enumset.MemberAuditorType;

@AllArgsConstructor
@Getter
public class MemberAuditorEvent {

    private Member member;

    private MemberAuditorType type;

}
