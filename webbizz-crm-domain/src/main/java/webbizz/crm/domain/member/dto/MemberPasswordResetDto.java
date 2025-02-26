package webbizz.crm.domain.member.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class MemberPasswordResetDto {

    @NotNull(message = "회원 정보가 없습니다.")
    private Long id;

    private String password;

    private String passwordConfirm;

}
