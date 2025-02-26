package webbizz.crm.domain.survey.enumset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.converter.JsonEnumConverter;
import webbizz.crm.domain.BaseEnumSet;

import javax.persistence.Converter;
import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum AnswerType implements BaseEnumSet {

    NONE(null, "", 0),
    RADIO("RADIO", "단일 선택", 1),
    CHECKBOX("CHECKBOX", "다중 선택", 2),
    TEXT("TEXT", "주관식", 3);

    private final String code;
    private final String detail;
    private final int value;

    @Converter(autoApply = true)
    public static class AnswerTypeConverter extends JsonEnumConverter<AnswerType> {
    }

}
