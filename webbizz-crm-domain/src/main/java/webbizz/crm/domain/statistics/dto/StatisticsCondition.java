package webbizz.crm.domain.statistics.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import webbizz.crm.util.PatternUtils;

import java.time.LocalDate;

@Getter
@Setter
public class StatisticsCondition {

    @DateTimeFormat(pattern = PatternUtils.DATE_FORMAT)
    private LocalDate startDate;

    @DateTimeFormat(pattern = PatternUtils.DATE_FORMAT)
    private LocalDate endDate;

}
