package webbizz.crm.domain.menu.enumset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.converter.JsonEnumConverter;
import webbizz.crm.domain.BaseEnumSet;

import javax.persistence.Converter;

@AllArgsConstructor
@Getter
public enum MenuMode implements BaseEnumSet {

    NONE(null, "", 0),
    LIST("LIST", "목록", 1),
    READ("READ", "상세", 2),
    WRITE("WRITE", "작성", 3),
    EDIT("EDIT", "수정", 4);

    private final String code;
    private final String detail;
    private final int value;


    @Converter(autoApply = true)
    public static class EnumConverter extends JsonEnumConverter<MenuMode> {}

}
