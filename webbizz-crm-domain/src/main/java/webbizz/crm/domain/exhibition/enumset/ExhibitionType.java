package webbizz.crm.domain.exhibition.enumset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.converter.JsonEnumConverter;
import webbizz.crm.domain.BaseEnumSet;

import javax.persistence.Converter;

@AllArgsConstructor
@Getter
public enum ExhibitionType implements BaseEnumSet {

    NONE(null, "", 0),
    MAIN("MAIN", "메인 배너", 1),
    POPUP("POPUP", "팝업 배너", 2),
    TOP("TOP", "상단 띠 배너", 3),
    PROMOTION("PROMOTION", "홍보 배너", 4),
    PROMOTION_2("PROMOTION_2", "홍보 배너 2", 5);

    private final String code;
    private final String detail;
    private final int value;


    @Converter(autoApply = true)
    public static class EnumConverter extends JsonEnumConverter<ExhibitionType> {}

}
