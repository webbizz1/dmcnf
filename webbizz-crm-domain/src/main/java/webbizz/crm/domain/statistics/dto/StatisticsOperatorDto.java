package webbizz.crm.domain.statistics.dto;

import lombok.Getter;
import webbizz.crm.domain.member.enumset.MemberAuditorType;

import java.time.LocalDateTime;

@Getter
public class StatisticsOperatorDto {

    private String loginId;

    private String realName;

    private String remoteAddr;

    private LocalDateTime createdAt;

    private MemberAuditorType type;

}
