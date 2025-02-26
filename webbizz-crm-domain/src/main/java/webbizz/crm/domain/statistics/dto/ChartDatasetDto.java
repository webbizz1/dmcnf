package webbizz.crm.domain.statistics.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ChartDatasetDto {

    private String label;

    private List<BigDecimal> data;

}
