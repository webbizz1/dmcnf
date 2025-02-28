package webbizz.crm.domain.exhibition.enumset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.converter.JsonEnumConverter;
import webbizz.crm.domain.BaseEnumSet;

import javax.persistence.Converter;

@AllArgsConstructor
@Getter
public enum LinkTarget implements BaseEnumSet {

    NONE(null, "링크 없음", 0),
    SELF("SELF", "현재 창", 1),
    BLANK("BLANK", "새 창", 2);

    private final String code;
    private final String detail;
    private final int value;


    @Converter(autoApply = true)
    public static class EnumConverter extends JsonEnumConverter<LinkTarget> {}

}
