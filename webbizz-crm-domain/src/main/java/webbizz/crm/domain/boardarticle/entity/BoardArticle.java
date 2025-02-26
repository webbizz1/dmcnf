package webbizz.crm.domain.boardarticle.entity;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.UpdatedBaseEntity;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.attachment.dto.AttachmentDto;
import webbizz.crm.domain.board.entity.Board;
import webbizz.crm.domain.member.entity.Member;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 게시물
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "board_article")
public class BoardArticle extends UpdatedBaseEntity {

    /**
     * 게시물 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_article_id")
    private Long id;

    /**
     * 게시판
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    /**
     * 회원 (NULL: 비회원)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 작성자 (회원: member.real_name)
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "writer_name")
    private String writerName;

    /**
     * 비밀번호 (비회원 전용)
     */
    @Size(max = 255)
    @Column(name = "password")
    private String password;

    /**
     * 제목
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "title")
    private String title;

    /**
     * 내용
     */
    @Lob
    @NotNull
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    /**
     * 첨부파일 JSON [다중]
     */
    @Column(name = "attachments_json", columnDefinition = "MEDIUMTEXT")
    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> attachments;

    /**
     * 첨부파일 이미지 JSON [다중]
     */
    @Column(name = "images_json", columnDefinition = "MEDIUMTEXT")
    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> images;

    /**
     * 첨부파일 PDF JSON [단일]
     */
    @Column(name = "pdf_json", columnDefinition = "MEDIUMTEXT")
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto pdf;

    /**
     * 조회 수
     */
    @Column(name = "read_count")
    @Builder.Default
    private Integer readCount = 0;

    /**
     * 비밀글 여부
     */
    @NotNull
    @Column(name = "secret_yn", columnDefinition = "CHAR")
    @Builder.Default
    private YN secretYn = YN.N;

    /**
     * 공지 여부
     */
    @NotNull
    @Column(name = "notice_yn", columnDefinition = "CHAR")
    @Builder.Default
    private YN noticeYn = YN.N;

    /**
     * 노출 여부
     */
    @NotNull
    @Column(name = "view_yn", columnDefinition = "CHAR")
    @Builder.Default
    private YN viewYn = YN.Y;

}
