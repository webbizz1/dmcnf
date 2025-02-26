package webbizz.crm.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class StatisticsBoardArticleItemDto {

    private String date;

    private BigDecimal value;

}
