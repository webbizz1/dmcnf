package webbizz.crm.domain.terms.enumset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.converter.JsonEnumConverter;
import webbizz.crm.domain.BaseEnumSet;

import javax.persistence.Converter;

@AllArgsConstructor
@Getter
public enum TermsType implements BaseEnumSet {

    NONE(null, "링크 없음", 0),
    TERMS("TERMS", "이용약관", 1),
    PRIVACY("PRIVACY", "개인정보처리방침", 2);

    private final String code;
    private final String detail;
    private final int value;


    @Converter(autoApply = true)
    public static class EnumConverter extends JsonEnumConverter<TermsType> {}

}
