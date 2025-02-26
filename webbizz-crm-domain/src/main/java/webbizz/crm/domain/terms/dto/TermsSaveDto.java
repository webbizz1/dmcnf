package webbizz.crm.domain.terms.dto;

import lombok.Getter;
import webbizz.crm.domain.terms.enumset.TermsType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class TermsSaveDto {

    private Long id;

    @NotNull(message = "약관 유형 정보가 없습니다.")
    private TermsType type;

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

}
