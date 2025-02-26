package webbizz.crm.domain.member.enumset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import webbizz.crm.converter.JsonEnumConverter;
import webbizz.crm.domain.BaseEnumSet;

import javax.persistence.Converter;

@AllArgsConstructor
@Getter
public enum MemberStatus implements BaseEnumSet {

    NONE(null, "", 0),
    NORMAL("NORMAL", "정상", 1),
    LOCKED("LOCKED", "잠김", 2),
    SUSPENDED("SUSPENDED", "정지", 3),
    WITHDRAWAL("WITHDRAWAL", "탈퇴", 4);

    private final String code;
    private final String detail;
    private final int value;


    @Converter(autoApply = true)
    public static class EnumConverter extends JsonEnumConverter<MemberStatus> {}

}
