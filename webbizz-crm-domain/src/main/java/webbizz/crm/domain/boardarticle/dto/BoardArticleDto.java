package webbizz.crm.domain.boardarticle.dto;

import lombok.Getter;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.attachment.dto.AttachmentDto;

import javax.persistence.Convert;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardArticleDto {

    private Long id;

    private Long boardId;

    private Long memberId;

    private String writerName;

    private String title;

    private String content;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> attachments;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> images;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto pdf;

    private Integer readCount;

    private YN secretYn;

    private YN noticeYn;

    private YN viewYn;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
