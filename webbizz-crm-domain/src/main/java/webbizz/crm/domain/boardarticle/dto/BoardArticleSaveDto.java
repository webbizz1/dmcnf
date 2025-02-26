package webbizz.crm.domain.boardarticle.dto;

import lombok.Getter;
import lombok.Setter;
import webbizz.crm.domain.YN;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardArticleSaveDto {

    @Setter
    private Long id;

    @NotNull(message = "게시판 정보가 없습니다.")
    private Long boardId;

    private Long memberId;

    @Setter
    private String writerName;

    private String password;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 255, message = "제목을 255자 이내로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private final List<String> attachmentUUIDs = new ArrayList<>();

    private final List<String> imageUUIDs = new ArrayList<>();

    private String pdfUUID;

    private YN secretYn;

    private YN noticeYn;

    @NotNull(message = "노출 상태를 선택해 주세요.")
    private YN viewYn;

    private String captchaResponse;

}
