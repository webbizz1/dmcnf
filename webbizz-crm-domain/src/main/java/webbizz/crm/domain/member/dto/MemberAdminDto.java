package webbizz.crm.domain.member.dto;

import lombok.Getter;
import webbizz.crm.domain.member.enumset.MemberStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MemberAdminDto {

    private Long id;

    private String loginId;

    private MemberStatus status;

    private String realName;

    private String email;

    private String telephoneNumber;

    private String mobileNumber;

    private List<String> allowIpAddresses;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private final List<Long> authorityPatternIds = new ArrayList<>();

}
