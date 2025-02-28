package webbizz.crm.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import webbizz.crm.domain.member.enumset.MemberStatus;
import webbizz.crm.util.PatternUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
public class MemberAdminSaveDto {

    @Setter
    private Long id;

    @NotBlank(message = "로그인 아이디를 입력해주세요.")
    private String loginId;

    private String password;

    private String passwordConfirm;

    @NotBlank(message = "이름을 입력해주세요.")
    private String realName;

    private String telephoneNumber;

    private String mobileNumber;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = PatternUtils.REGEX_EMAIL, message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private List<String> allowIpAddresses;

    @NotEmpty(message = "1개 이상의 메뉴 권한을 선택해 주세요.")
    private List<Long> authorityPatternIds;

    private MemberStatus status;

}
