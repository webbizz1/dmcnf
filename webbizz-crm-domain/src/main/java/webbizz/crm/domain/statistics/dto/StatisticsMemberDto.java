package webbizz.crm.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class StatisticsMemberDto {

    private List<StatisticsMemberItemDto> items;

    private List<LocalDate> labels;

    private List<ChartDatasetDto> datasets;

}
