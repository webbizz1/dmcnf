package webbizz.crm.domain.board.dto;

import lombok.Getter;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.board.enumset.BoardType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

@Getter
public class BoardSaveDto {

    private Long id;

    @NotNull(message = "게시판 유형을 선택해 주세요.")
    private BoardType type;

    @NotBlank(message = "게시판 이름을 입력해 주세요.")
    @Size(max = 255, message = "게시판 이름을 255자 이내로 입력해 주세요.")
    private String name;

    private String description;

    private YN useDescriptionYn;

    @NotNull(message = "노출 상태를 선택해 주세요.")
    private YN viewYn;

    private Map<String, String> config;

}
