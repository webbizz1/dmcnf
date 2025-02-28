package webbizz.crm.domain.statistics.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import webbizz.crm.domain.PageCondition;
import webbizz.crm.domain.member.enumset.MemberAuditorType;
import webbizz.crm.util.PatternUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class StatisticsOperatorCondition extends PageCondition {

    @NotNull
    private MemberAuditorType type;

    private String loginId;

    private String realName;

    @DateTimeFormat(pattern = PatternUtils.DATE_FORMAT)
    private LocalDate startDate;

    @DateTimeFormat(pattern = PatternUtils.DATE_FORMAT)
    private LocalDate endDate;

}
