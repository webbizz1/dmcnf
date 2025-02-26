package webbizz.crm.domain.boardconfig.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.BaseEntity;
import webbizz.crm.domain.board.entity.Board;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 게시판 설정
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "board_config")
public class BoardConfig extends BaseEntity {

    /**
     * 게시판 설정 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_config_id")
    private Long id;

    /**
     * 게시판
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    /**
     * 설정 키
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "config_key")
    private String key;

    /**
     * 설정 값
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "config_value")
    private String value;

}
