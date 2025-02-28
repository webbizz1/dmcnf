package webbizz.crm.service.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.*;
import webbizz.crm.exception.ApiInternalServerErrorException;
import webbizz.crm.service.excel.annotation.ExcelColumn;
import webbizz.crm.service.excel.style.ExcelCellStyle;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <h6>애노테이션 기반 엑셀 파일 생성 유틸리티</h6>
 * <p>
 * DTO 클래스에 {@link ExcelColumn @ExcelColumn} 애노테이션을 사용하여 엑셀 파일의 헤더와 본문을 지정한다.
 *
 * <p>사용 예시:</p>
 *
 * DTO 클래스에 다음과 같이 애노테이션을 지정한다.
 * <pre>{@code
 *     @ExcelColumn(
 *         columnPixel = 75, // 컬럼 너비 (픽셀)
 *         headerName = "제목", // 헤더 이름
 *         headerStyle = ExcelCellHeaderStyle.class, // 헤더 스타일
 *         bodyStyle = ExcelCellCenterStyle.class // 본문 스타일
 *     )
 *     private String title;
 * }</pre>
 * <p>열 적용 순서는 DTO 클래스 내부에 선언된 순서 기준으로 적용되며, domain 모듈에서는 POI 라이브러리 종속성이 없기 때문에
 * service 모듈에서 dto 내부에 엑셀 전용 DTO 파일을 만들어서 변환 후 사용하는 것을 권장한다.<br />
 * 예: {@code MemberDto.class} (domain 패키지) -> {@code MemberExcelDto.class} (service 패키지)</p>
 *
 * <p>서비스 클래스에서 다음과 같이 사용한다: (data 타입은 ExcelDto, ExcelDto 는 DTO 클래스)</p>
 * <pre>{@code return new ExcelFile<>(data, "시트 이름", ExcelDto.class);}</pre>
 *
 * <p>컨트롤러 클래스에서 파일로 다운로드 응답을 주려면 다음과 같이 사용한다:</p>
 * <pre>{@code
 *     return excelFile.toResponseEntity();
 *     // 또는
 *     return excelFile.toResponseEntity(fileName);
 * }</pre>
 *
 * <p>{@code headerStyle}, {@code bodyStyle} 을 커스텀 하고 싶다면,
 * {@link ExcelCellStyle} 클래스를 상속받아 {@code apply} 메서드를 오버라이드하여 사용한다.</p>
 *
 * <p>만약 {@link Workbook} 객체를 이미 만들었고 다운로드 응답만 주고 싶다면 다음과 같이 사용한다:</p>
 * <pre>{@code
 *     return new ExcelFile(workbook).toResponseEntity();
 *     // 또는
 *     return new ExcelFile(workbook).toResponseEntity(fileName);
 * }</pre>
 *
 * @since 2024-11-28
 * @author TAEROK HWANG
 * @version 1.0
 * @param <T> 데이터 타입
 */
public class ExcelFile<T> {

    private static final int ROW_START_INDEX = 0;
    private static final int COLUMN_START_INDEX = 0;

    private final Workbook workbook;
    private final Sheet sheet;

    private String fileName;

    private final Map<String, String> fields = new LinkedHashMap<>();
    private final Map<String, Integer> widths = new LinkedHashMap<>();

    private final Set<Class<? extends ExcelCellStyle>> declaredStyles = new HashSet<>();
    private final Map<Class<? extends ExcelCellStyle>, CellStyle> styleCache = new HashMap<>();

    private final Map<String, Class<? extends ExcelCellStyle>> headerStyles = new HashMap<>();
    private final Map<String, Class<? extends ExcelCellStyle>> bodyStyles = new HashMap<>();

    public ExcelFile(List<T> data, String sheetName, Class<T> clazz) {
        this.workbook = new XSSFWorkbook();
        this.sheet = this.workbook.createSheet(sheetName);
        this.sheet.setDefaultColumnWidth(10);
        this.fileName = sheetName;

        this.initializeAnnotation(clazz);
        this.renderExcel(data);
    }

    public ExcelFile(Workbook workbook) {
        this.workbook = workbook;
        this.sheet = workbook.getSheetAt(0);
    }

    /**
     * 클래스 필드의 애노테이션을 읽고 관련 정보를 초기화
     *
     * @param clazz 클래스
     * @author TAEROK HWANG
     */
    private void initializeAnnotation(Class<T> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);

                this.fields.put(field.getName(), annotation.headerName());
                this.widths.put(field.getName(), annotation.columnPixel());

                this.declaredStyles.add(annotation.headerStyle());
                this.declaredStyles.add(annotation.bodyStyle());

                this.headerStyles.put(field.getName(), annotation.headerStyle());
                this.bodyStyles.put(field.getName(), annotation.bodyStyle());
            }
        }
    }

    /**
     * 엑셀 파일 생성
     *
     * @param data 데이터
     * @author TAEROK HWANG
     */
    private void renderExcel(List<T> data) {
        this.cacheStyles();
        this.renderHeaders();

        if (data.isEmpty()) {
            return;
        }

        int rowIndex = ROW_START_INDEX + 1;
        for (T item : data) {
            this.renderBody(item, rowIndex++);
        }
    }

    /**
     * 선언된 모든 스타일을 시트에 생성하고 캐싱
     *
     * @author TAEROK HWANG
     */
    private void cacheStyles() {
        for (Class<? extends ExcelCellStyle> clazz : this.declaredStyles) {
            try {
                ExcelCellStyle excelCellStyle = clazz.getDeclaredConstructor().newInstance();
                CellStyle cellStyle = this.workbook.createCellStyle();
                excelCellStyle.apply(cellStyle, this.workbook);
                this.styleCache.put(clazz, cellStyle);
            } catch (Exception e) {
                throw new ApiInternalServerErrorException("Failed to create style: " + clazz.getName());
            }
        }
    }

    /**
     * 헤더 렌더링
     *
     * @author TAEROK HWANG
     */
    private void renderHeaders() {
        Row row = this.sheet.createRow(ROW_START_INDEX);
        int columnIndex = COLUMN_START_INDEX;

        for (Map.Entry<String, String> entry : this.fields.entrySet()) {
            Cell cell = row.createCell(columnIndex);
            cell.setCellValue(entry.getValue()); // 헤더는 문자열 고정이므로 바로 지정

            Class<? extends ExcelCellStyle> clazz = headerStyles.get(entry.getKey());
            cell.setCellStyle(styleCache.get(clazz));

            // 컬럼 너비 설정
            this.sheet.setColumnWidth(columnIndex, 256 * this.widths.get(entry.getKey()) / 6);

            columnIndex++;
        }
    }

    /**
     * 데이터 렌더링
     *
     * @param item 데이터
     * @param rowIndex 행 인덱스
     * @author TAEROK HWANG
     */
    private void renderBody(T item, int rowIndex) {
        Row row = this.sheet.createRow(rowIndex);
        int columnIndex = COLUMN_START_INDEX;

        for (String fieldName : this.fields.keySet()) {
            Cell cell = row.createCell(columnIndex++);

            try {
                Field field = item.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                this.setCellValue(cell, field.get(item));
                this.setCellStyle(cell, fieldName);

                Class<? extends ExcelCellStyle> clazz = bodyStyles.get(fieldName);
                cell.setCellStyle(styleCache.get(clazz));
            } catch (Exception e) {
                throw new RuntimeException("Error rendering cell for field: " + fieldName);
            }
        }
    }

    /**
     * 셀에 값을 설정
     *
     * @param cell 셀
     * @param value 값
     * @author TAEROK HWANG
     */
    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            return;
        }

        if (value instanceof Number) {
            Number number = (Number) value;
            cell.setCellValue(number.doubleValue());
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * 셀 스타일 설정
     *
     * @param cell 셀
     * @param fieldName 필드 이름
     * @author TAEROK HWANG
     */
    private void setCellStyle(Cell cell, String fieldName) {
        Class<? extends ExcelCellStyle> clazz = this.bodyStyles.get(fieldName);
        cell.setCellStyle(styleCache.get(clazz));
    }

    /**
     * 엑셀 파일을 ResponseEntity 로 변환
     *
     * @return ResponseEntity<byte[]>
     * @author TAEROK HWANG
     */
    public ResponseEntity<byte[]> toResponseEntity() {
        return this.toResponseEntity(this.fileName);
    }

    /**
     * 엑셀 파일을 ResponseEntity 로 변환 (파일 이름 지정)
     *
     * @param fileName 파일 이름
     * @return ResponseEntity<byte[]>
     * @author TAEROK HWANG
     */
    public ResponseEntity<byte[]> toResponseEntity(String fileName) {
        String nowString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            workbook.write(byteArrayOutputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment().filename(String.format("%s_%s.xlsx",
                                            fileName, nowString), StandardCharsets.UTF_8)
                                    .build().toString())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(byteArrayOutputStream.size())
                    .cacheControl(CacheControl.noCache())
                    .body(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new ApiInternalServerErrorException(e.getMessage());
        }
    }

}
