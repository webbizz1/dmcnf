package webbizz.crm.domain.menu.enumset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.converter.JsonEnumConverter;
import webbizz.crm.domain.BaseEnumSet;

import javax.persistence.Converter;

@AllArgsConstructor
@Getter
public enum MenuType implements BaseEnumSet {

    NONE(null, "", 0),
    LINK("LINK", "링크", 1),
    CONTENT("CONTENT", "콘텐츠", 2),
    BOARD("BOARD", "게시판", 3);

    private final String code;
    private final String detail;
    private final int value;


    @Converter(autoApply = true)
    public static class EnumConverter extends JsonEnumConverter<MenuType> {}

}
