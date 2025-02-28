package webbizz.crm.service.excel.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import webbizz.crm.util.PatternUtils;

public class ExcelCellDateStyle extends ExcelCellStyle {
    @Override
    public void apply(CellStyle cellStyle, Workbook workbook) {
        super.apply(cellStyle, workbook);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setDataFormat(workbook.createDataFormat().getFormat(PatternUtils.DATE_FORMAT));
    }
}
