package webbizz.crm.domain.board.entity;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.UpdatedBaseEntity;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.board.enumset.BoardType;
import webbizz.crm.domain.boardarticle.entity.BoardArticle;
import webbizz.crm.domain.boardconfig.entity.BoardConfig;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 게시판
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "board")
public class Board extends UpdatedBaseEntity {

    /**
     * 게시판 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    /**
     * 게시판 유형 [TEXT 텍스트형 | GALLERY 갤러리형 | VIEWER 뷰어형]
     */
    @NotNull
    @Column(name = "board_type")
    private BoardType type;

    /**
     * 게시판 이름
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "board_name")
    private String name;

    /**
     * 게시판 소개글 [에디터]
     */
    @Lob
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    /**
     * 게시판 소개글 사용 여부
     */
    @NotNull
    @Column(name = "use_description_yn", columnDefinition = "CHAR")
    @Builder.Default
    private YN useDescriptionYn = YN.N;

    /**
     * 노출 여부
     */
    @NotNull
    @Column(name = "view_yn", columnDefinition = "CHAR")
    @Builder.Default
    private YN viewYn = YN.Y;


    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<BoardConfig> configs;

    @OneToMany(mappedBy = "board")
    private List<BoardArticle> articles;

    public void setConfigs(final List<BoardConfig> configs) {
        this.configs = configs;
        configs.forEach(config -> config.setBoard(this));
    }

}
