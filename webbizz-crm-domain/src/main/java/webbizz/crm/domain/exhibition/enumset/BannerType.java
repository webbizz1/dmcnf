package webbizz.crm.domain.exhibition.enumset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.converter.JsonEnumConverter;
import webbizz.crm.domain.BaseEnumSet;

import javax.persistence.Converter;

@AllArgsConstructor
@Getter
public enum BannerType implements BaseEnumSet {

    NONE(null, "", 0),
    TEXT("TEXT", "텍스트", 1),
    IMAGE("IMAGE", "이미지", 2);

    private final String code;
    private final String detail;
    private final int value;


    @Converter(autoApply = true)
    public static class EnumConverter extends JsonEnumConverter<BannerType> {}

}
