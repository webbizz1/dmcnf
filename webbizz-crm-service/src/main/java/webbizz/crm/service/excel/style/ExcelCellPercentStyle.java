package webbizz.crm.service.excel.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelCellPercentStyle extends ExcelCellNumberStyle {
    @Override
    public void apply(CellStyle cellStyle, Workbook workbook) {
        super.apply(cellStyle, workbook);
        cellStyle.setDataFormat(workbook.createDataFormat().getFormat("0.0%_-"));
    }
}
