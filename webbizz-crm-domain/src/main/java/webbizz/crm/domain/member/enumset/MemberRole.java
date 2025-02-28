package webbizz.crm.domain.member.enumset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.converter.JsonEnumConverter;
import webbizz.crm.domain.BaseEnumSet;

import javax.persistence.Converter;

@AllArgsConstructor
@Getter
public enum MemberRole implements BaseEnumSet {

    NONE(null, "", 0),
    USER("ROLE_USER", "회원", -1),
    ADMIN("ROLE_ADMIN", "관리자", -2);

    private final String code;
    private final String detail;
    private final int value;


    @Converter(autoApply = true)
    public static class EnumConverter extends JsonEnumConverter<MemberRole> {}

}
