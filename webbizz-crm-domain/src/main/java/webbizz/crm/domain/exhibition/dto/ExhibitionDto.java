package webbizz.crm.domain.exhibition.dto;

import lombok.Getter;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.attachment.dto.AttachmentDto;
import webbizz.crm.domain.exhibition.enumset.BannerType;
import webbizz.crm.domain.exhibition.enumset.ExhibitionType;
import webbizz.crm.domain.exhibition.enumset.LinkTarget;

import java.time.LocalDateTime;

@Getter
public class ExhibitionDto {

    private Long id;

    private ExhibitionType type;

    private BannerType bannerType;

    private String title;

    private String linkUrl;

    private LinkTarget linkTarget;

    private AttachmentDto pcImage;

    private AttachmentDto mobileImage;

    private LocalDateTime viewStartAt;

    private LocalDateTime viewEndAt;

    private Integer viewOrder;

    private YN viewYn;

    private LocalDateTime createdAt;

}
