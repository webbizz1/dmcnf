package webbizz.crm.service.excel.annotation;

import webbizz.crm.service.excel.style.ExcelCellHeaderStyle;
import webbizz.crm.service.excel.style.ExcelCellStyle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {

    /**
     * 컬럼 너비를 픽셀 단위로 지정
     */
    int columnPixel() default 75;

    /**
     * 헤더 이름
     */
    String headerName() default "";

    /**
     * 적용할 제목 스타일
     */
    Class<? extends ExcelCellStyle> headerStyle() default ExcelCellHeaderStyle.class;

    /**
     * 적용할 본문 스타일
     */
    Class<? extends ExcelCellStyle> bodyStyle() default ExcelCellStyle.class;

}
