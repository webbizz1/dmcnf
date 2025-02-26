package webbizz.crm.domain.boardconfig.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class BoardConfigSaveDto {

    @NotBlank
    private String key;

    @NotBlank
    private String value;

}
