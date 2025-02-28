package webbizz.crm.domain.boardcomment.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.UpdatedBaseEntity;
import webbizz.crm.domain.boardarticle.entity.BoardArticle;
import webbizz.crm.domain.member.entity.Member;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 게시물 댓글
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "board_comment")
public class BoardComment extends UpdatedBaseEntity {

    /**
     * 게시물 댓글 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_comment_id")
    private Long id;

    /**
     * 게시물
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_article_id", nullable = false)
    private BoardArticle boardArticle;

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
     * 내용 [에디터]
     */
    @Lob
    @Column(name = "content")
    private String content;

}
