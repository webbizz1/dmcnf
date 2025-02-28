package webbizz.crm.domain.member.enumset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.converter.JsonEnumConverter;
import webbizz.crm.domain.BaseEnumSet;

import javax.persistence.Converter;

@AllArgsConstructor
@Getter
public enum MemberAuditorType implements BaseEnumSet {

    NONE(null, "", 0),
    MODIFICATION("MODIFICATION", "정보 수정", 1),
    LOGIN("LOGIN", "로그인", 11);

    private final String code;
    private final String detail;
    private final int value;


    @Converter(autoApply = true)
    public static class EnumConverter extends JsonEnumConverter<MemberAuditorType> {}

}
