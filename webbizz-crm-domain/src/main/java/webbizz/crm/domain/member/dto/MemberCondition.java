package webbizz.crm.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import webbizz.crm.domain.PageCondition;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.member.enumset.MemberRole;
import webbizz.crm.domain.member.enumset.MemberStatus;
import webbizz.crm.util.PatternUtils;

import java.time.LocalDate;

@Getter
@Setter
public class MemberCondition extends PageCondition {

    private MemberRole role;

    private MemberStatus status;

    private String loginId;

    private String realName;

    private String telephoneNumber;

    private String mobileNumber;

    private String searchField;

    private String searchText;

    @DateTimeFormat(pattern = PatternUtils.DATE_FORMAT)
    private LocalDate startDate;

    @DateTimeFormat(pattern = PatternUtils.DATE_FORMAT)
    private LocalDate endDate;

    private YN delYn = YN.N;

}
