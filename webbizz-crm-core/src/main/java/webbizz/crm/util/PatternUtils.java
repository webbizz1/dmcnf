package webbizz.crm.util;

public class PatternUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_MILLS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";

    /** 이메일 정규식 */
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /** IPv4 주소 정규식 */
    public static final String REGEX_IP_ADDRESS = "^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$";

    /** 휴대폰 번호 정규식 */
    public static final String REGEX_MOBILE_NUMBER = "^010(17\\d{6}|[2-9]\\d{7})|01[16789]([2-8]\\d{6}|9\\d{7})$";

    /** 전화번호 (휴대폰 포함) 정규식 */
    public static final String REGEX_TELEPHONE_NUMBER = "(^01[016789]|02|03[1-3]|04[1-4]|050[1-9]|05[1-5]|06[1-4]|0[78]0)([1-9][0-9]{2,3})([0-9]{4})$";

}
