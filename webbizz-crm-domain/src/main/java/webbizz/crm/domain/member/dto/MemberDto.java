package webbizz.crm.domain.member.dto;

import lombok.Getter;
import webbizz.crm.domain.member.enumset.MemberStatus;

import java.time.LocalDateTime;

@Getter
public class MemberDto {

    private Long id;

    private String loginId;

    private MemberStatus status;

    private String realName;

    private String email;

    private String telephoneNumber;

    private String mobileNumber;

    private LocalDateTime lastLoginAt;

    private String withdrawalReason;

    private LocalDateTime withdrawalAt;

    private LocalDateTime createdAt;

}
