package webbizz.crm.domain;

import java.util.EnumSet;

public interface BaseEnumSet {

    String getCode();

    String getDetail();

    int getValue();

    static <E extends Enum<E> & BaseEnumSet> E of(Class<E> enumClass, String code) {
        return EnumSet.allOf(enumClass).stream()
                .filter(v -> v.getCode() != null && v.getCode().equalsIgnoreCase(code))
                .findAny()
                .orElse(enumClass.getEnumConstants().length > 0 ? enumClass.getEnumConstants()[0] : null);
    }

}
