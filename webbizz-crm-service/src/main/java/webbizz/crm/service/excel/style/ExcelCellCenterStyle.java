package webbizz.crm.service.excel.style;

import org.apache.poi.ss.usermodel.*;

public class ExcelCellCenterStyle extends ExcelCellStyle {
    @Override
    public void apply(CellStyle cellStyle, Workbook workbook) {
        super.apply(cellStyle, workbook);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
    }
}
