package webbizz.crm.service.statistics.dto;

import lombok.AllArgsConstructor;
import webbizz.crm.service.excel.annotation.ExcelColumn;
import webbizz.crm.service.excel.style.ExcelCellCenterStyle;
import webbizz.crm.service.excel.style.ExcelCellDateTimeStyle;
import webbizz.crm.service.excel.style.ExcelCellNumberStyle;

import java.time.LocalDateTime;

@AllArgsConstructor
public class StatisticsOperatorExcelDto {

    @ExcelColumn(
            columnPixel = 45,
            headerName = "번호",
            bodyStyle = ExcelCellNumberStyle.class
    )
    private Long id;

    @ExcelColumn(
            columnPixel = 70,
            headerName = "아이디"
    )
    private String loginId;

    @ExcelColumn(
            columnPixel = 70,
            headerName = "이름"
    )
    private String realName;

    @ExcelColumn(
            columnPixel = 100,
            headerName = "발생 IP",
            bodyStyle = ExcelCellCenterStyle.class
    )
    private String remoteAddr;

    @ExcelColumn(
            columnPixel = 120,
            headerName = "발생 IP",
            bodyStyle = ExcelCellDateTimeStyle.class
    )
    private LocalDateTime createdAt;

    @ExcelColumn(
            columnPixel = 100,
            headerName = "내용"
    )
    private String eventName;

}
