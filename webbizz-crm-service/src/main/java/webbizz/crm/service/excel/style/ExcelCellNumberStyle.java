package webbizz.crm.service.excel.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelCellNumberStyle extends ExcelCellStyle {
    @Override
    public void apply(CellStyle cellStyle, Workbook workbook) {
        super.apply(cellStyle, workbook);
        cellStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0_-"));
        cellStyle.setWrapText(false);
    }
}
