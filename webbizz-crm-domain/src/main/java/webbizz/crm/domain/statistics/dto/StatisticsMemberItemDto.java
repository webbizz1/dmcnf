package webbizz.crm.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class StatisticsMemberItemDto {

    private LocalDate date;

    private BigDecimal value;

    private BigDecimal ratio;

}
