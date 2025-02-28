package webbizz.crm.service.excel.style;

import org.apache.poi.ss.usermodel.*;

public class ExcelCellHeaderStyle extends ExcelCellStyle {
    @Override
    public void apply(CellStyle cellStyle, Workbook workbook) {
        super.apply(cellStyle, workbook);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setWrapText(false);
    }
}
