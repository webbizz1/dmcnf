package webbizz.crm.domain.boardarticle.dto;

import lombok.Getter;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.attachment.dto.AttachmentDto;

import javax.persistence.Convert;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardArticleListDto {

    private Long id;

    private String boardName;

    private String title;

    private String content;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> images;

    private YN secretYn;

    private YN noticeYn;

    private YN viewYn;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long menuId;
}
