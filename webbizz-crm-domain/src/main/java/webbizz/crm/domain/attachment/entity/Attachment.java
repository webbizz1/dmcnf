package webbizz.crm.domain.attachment.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.UpdatedBaseEntity;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.attachment.dto.AttachmentDto;

import javax.persistence.*;

/**
 * 첨부파일
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "attachment")
public class Attachment extends UpdatedBaseEntity {

    /**
     * 첨부파일 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long id;

    /**
     * 파일 UUID
     */
    @Column(name = "uuid", columnDefinition = "CHAR(36)")
    private String uuid;

    /**
     * 저장된 파일 이름 (확장자 포함)
     */
    @Column(name = "upload_name")
    private String uploadName;

    /**
     * 원본 파일 이름
     */
    @Column(name = "original_name")
    private String originalName;

    /**
     * 파일 확장자 (.제외)
     */
    @Column(name = "extension")
    private String extension;

    /**
     * 저장된 파일 경로
     */
    @Column(name = "path")
    private String path;

    /**
     * 실제 URL
     */
    @Column(name = "real_url")
    private String realUrl;

    /**
     * 파일 크기
     */
    @Column(name = "file_size")
    private Long size;

    /**
     * 업로드 파일 구분 값
     */
    @Column(name = "source")
    private String source;

    /**
     * 노출 여부
     */
    @Column(name = "view_yn", columnDefinition = "CHAR")
    private YN viewYn;

    /**
     * Entity 를 DTO 로 변환
     *
     * @return 변환된 DTO 객체
     * @author TAEROK HWANG
     */
    public AttachmentDto toDto() {
        return new AttachmentDto(
                this.id,
                this.uuid,
                this.uploadName,
                this.originalName,
                this.extension,
                this.path,
                this.realUrl,
                this.size,
                this.createdAt
        );
    }

}
