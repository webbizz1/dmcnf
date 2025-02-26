package webbizz.crm.domain.board.enumset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.converter.JsonEnumConverter;
import webbizz.crm.domain.BaseEnumSet;

import javax.persistence.Converter;

@AllArgsConstructor
@Getter
public enum BoardType implements BaseEnumSet {

    NONE(null, "", 0),
    TEXT("TEXT", "텍스트형", 1),
    GALLERY("GALLERY", "갤러리형", 2),
    VIEWER("VIEWER", "뷰어형", 3);

    private final String code;
    private final String detail;
    private final int value;


    @Converter(autoApply = true)
    public static class EnumConverter extends JsonEnumConverter<BoardType> {}

}
