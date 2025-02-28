package webbizz.crm.domain.board.enumset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import webbizz.crm.domain.BaseEnumSet;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.boardconfig.entity.BoardConfig;
import webbizz.crm.domain.member.enumset.MemberRole;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum BoardConfigKey {

    USE_ARTICLE_YN("글", "ARTICLE", getDefaultYn(), "Y"),
    USE_COMMENT_YN("댓글", "COMMENT", getDefaultYn(), "N"),
    USE_ATTACHMENT_YN("파일 첨부", "ATTACHMENT", getDefaultYn(), "N"),
    USE_NOTICE_YN("상단 공지", "NOTICE", getDefaultYn(), "N"),
    USE_CAPTCHA_YN("캡챠", "CAPTCHA", getDefaultYn(), "N"),

    GRANT_ARTICLE_LIST("목록 조회", null, getDefaultAuthorities(), BoardConfigKey.KEY_MEMBER_ANONYMOUS),
    GRANT_ARTICLE_READ("상세 조회", null, getDefaultAuthorities(), BoardConfigKey.KEY_MEMBER_ANONYMOUS),
    GRANT_ARTICLE_WRITE("글 쓰기", "ARTICLE", getDefaultAuthorities(), BoardConfigKey.KEY_MEMBER_USER),
    GRANT_COMMENT_WRITE("댓글 쓰기", "COMMENT", getDefaultAuthorities(), BoardConfigKey.KEY_MEMBER_USER);

    private final String detail;
    private final String group;
    private final String[] availableValues;
    private final String defaultValue;

    public static final String KEY_MEMBER_ANONYMOUS = "0";
    public static final String KEY_MEMBER_USER = "-1";
    public static final String KEY_MEMBER_ADMIN = "-2";

    public static final String[] DEFAULT_YN = new String[]{"Y", "N"};
    public static final String[] DEFAULT_AUTHORITIES = new String[]{
            BoardConfigKey.KEY_MEMBER_ANONYMOUS,
            BoardConfigKey.KEY_MEMBER_USER,
            BoardConfigKey.KEY_MEMBER_ADMIN
    };

    private static String[] getDefaultYn() {
        return BoardConfigKey.DEFAULT_YN;
    }

    private static String[] getDefaultAuthorities() {
        return BoardConfigKey.DEFAULT_AUTHORITIES;
    }

    /**
     * 기본값으로 설정된 Map 반환
     *
     * @return 기본값으로 설정된 Map (key: name, value: defaultValue)
     * @author TAEROK HWANG
     */
    public static Map<String, String> toDefaultMap() {
        return Arrays.stream(BoardConfigKey.values())
                .collect(Collectors.toMap(BoardConfigKey::name, BoardConfigKey::getDefaultValue));
    }

    /**
     * 설정값을 사용하는지 여부 반환
     *
     * @param config 설정 Map
     * @param key 설정 키
     * @return 설정값을 사용하는지 여부 (true: 사용, false: 미사용)
     * @author TAEROK HWANG
     * @see #isUseConfig(List, BoardConfigKey)
     */
    public static boolean isUseConfig(final List<BoardConfig> config, final BoardConfigKey key) {
        return BoardConfigKey.isUseConfig(config.stream()
                .collect(Collectors.toMap(BoardConfig::getKey, BoardConfig::getValue)), key);
    }

    /**
     * 설정값을 사용하는지 여부 반환
     *
     * @param config 설정 Map
     * @param key 설정 키
     * @return 설정값을 사용하는지 여부 (true: 사용, false: 미사용)
     * @throws IllegalArgumentException 설정 키가 USE_로 시작하지 않을 경우
     */
    public static boolean isUseConfig(final Map<String, String> config, final BoardConfigKey key) {
        if (config == null || key == null)
            return false;

        if (!key.name().startsWith("USE_"))
            throw new IllegalArgumentException("Only keys starting with 'USE_' can be used.");

        String setValue = config.getOrDefault(key.name(), key.getDefaultValue());
        return YN.of(setValue).isBool();
    }

    /**
     * 권한이 있는지 여부 반환
     *
     * @param config 설정 Map
     * @param key 설정 키
     * @param authorities 권한 목록 ({@link UserDetails#getAuthorities()})
     * @return 권한이 있는지 여부 (true: 권한 있음, false: 권한 없음)
     * @author TAEROK HWANG
     * @see #hasPermission(Map, BoardConfigKey, String)
     */
    public static boolean hasPermission(final Map<String, String> config,
                                        final BoardConfigKey key,
                                        final Collection<? extends GrantedAuthority> authorities) {

        String ownValue = (authorities == null || authorities.isEmpty()) ?
                BoardConfigKey.KEY_MEMBER_ANONYMOUS
                :
                String.valueOf(authorities.stream()
                        .filter(a -> a.getAuthority().startsWith("ROLE_"))
                        .findAny()
                        .map(a -> BaseEnumSet.of(MemberRole.class, a.getAuthority()))
                        .orElse(MemberRole.NONE) // ROLE 없으면 NONE
                        .getValue()); // 권한 값 반환 (int -> String)

        return BoardConfigKey.hasPermission(config, key, ownValue);
    }

    /**
     * 권한이 있는지 여부 반환
     *
     * @param config 설정 Map
     * @param key 설정 키
     * @param ownValue 소유한 권한 값
     * @return 권한이 있는지 여부 (true: 권한 있음, false: 권한 없음)
     * @author TAEROK HWANG
     */
    public static boolean hasPermission(Map<String, String> config, BoardConfigKey key, String ownValue) {
        if (config == null || key == null || ownValue == null)
            return false;

        if (!key.name().startsWith("GRANT_"))
            throw new IllegalArgumentException("Only keys starting with 'GRANT_' can be used.");

        String setValue = config.getOrDefault(key.name(), key.getDefaultValue());
        boolean isGranted = false;

        // 소유 권한 값이 양수이면 정확히 일치
        if (ownValue.matches("^[1-9]\\d*$")) {
            isGranted = ownValue.equals(setValue);
        }

        // 권한이 없고 소유 권한 값이 음수이면 ROLE 관련 권한 체크
        if (!isGranted) {
            /*
             * setValue (소유한 권한) < ownValue (설정된 권한) => 음수 (권한 없음)
             * setValue (소유한 권한) = ownValue (설정된 권한) => 0 (권한 있음)
             * setValue (소유한 권한) > ownValue (설정된 권한) => 양수 (권한 있음)
             */
            isGranted = setValue.compareTo(ownValue) >= 0;
        }

        return isGranted;
    }


}
