package webbizz.crm.service.statistics.dto;

import lombok.AllArgsConstructor;
import webbizz.crm.service.excel.annotation.ExcelColumn;
import webbizz.crm.service.excel.style.ExcelCellDateStyle;
import webbizz.crm.service.excel.style.ExcelCellNumberStyle;
import webbizz.crm.service.excel.style.ExcelCellPercentStyle;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
public class StatisticsMemberExcelDto {

    @ExcelColumn(
            columnPixel = 70,
            headerName = "날짜",
            bodyStyle = ExcelCellDateStyle.class
    )
    private LocalDate date;

    @ExcelColumn(
            columnPixel = 60,
            headerName = "가입자 수",
            bodyStyle = ExcelCellNumberStyle.class
    )
    private BigDecimal value;

    @ExcelColumn(
            columnPixel = 55,
            headerName = "비율",
            bodyStyle = ExcelCellPercentStyle.class
    )
    private BigDecimal ratio;

}
