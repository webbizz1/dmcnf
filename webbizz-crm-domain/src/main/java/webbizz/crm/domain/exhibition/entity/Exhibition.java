package webbizz.crm.domain.exhibition.entity;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.UpdatedBaseEntity;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.attachment.dto.AttachmentDto;
import webbizz.crm.domain.exhibition.enumset.BannerType;
import webbizz.crm.domain.exhibition.enumset.ExhibitionType;
import webbizz.crm.domain.exhibition.enumset.LinkTarget;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 전시 (배너, 팝업)
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "exhibition")
public class Exhibition extends UpdatedBaseEntity {

    /**
     * 전시 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exhibition_id")
    private Long id;

    /**
     * 전시 유형 [MAIN 메인 배너 | POPUP 팝업 배너 | TOP 상단 띠 배너]
     */
    @NotNull
    @Column(name = "exhibition_type")
    private ExhibitionType type;

    /**
     * 배너 유형 (전시 유형 TOP 전용) [TEXT 텍스트 | IMAGE 이미지]
     */
    @Column(name = "banner_type")
    private BannerType bannerType;

    /**
     * 제목
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "title")
    private String title;

    /**
     * 링크 URL
     */
    @Size(max = 511)
    @Column(name = "link_url")
    private String linkUrl;

    /**
     * 링크 타겟 [SELF 현재 창 | BLANK 새 창]
     */
    @Column(name = "link_target")
    private LinkTarget linkTarget;

    /**
     * PC 이미지 JSON [단일]
     */
    @Lob
    @Column(name = "pc_image_json", columnDefinition = "TEXT")
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto pcImage;

    /**
     * 모바일 이미지 JSON [단일]
     */
    @Lob
    @Column(name = "mobile_image_json", columnDefinition = "TEXT")
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto mobileImage;

    /**
     * 노출 시작 일시
     */
    @NotNull
    @Column(name = "view_start_at")
    private LocalDateTime viewStartAt;

    /**
     * 노출 종료 일시
     */
    @Column(name = "view_end_at")
    private LocalDateTime viewEndAt;

    /**
     * 노출 순서
     */
    @NotNull
    @Column(name = "view_order")
    @Builder.Default
    private Integer viewOrder = 255;

    /**
     * 노출 여부
     */
    @NotNull
    @Column(name = "view_yn", columnDefinition = "CHAR(1)")
    @Builder.Default
    private YN viewYn = YN.Y;

}
