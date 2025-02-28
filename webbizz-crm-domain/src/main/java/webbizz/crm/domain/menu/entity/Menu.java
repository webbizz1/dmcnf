package webbizz.crm.domain.menu.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.UpdatedBaseEntity;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.board.entity.Board;
import webbizz.crm.domain.menu.enumset.LinkTarget;
import webbizz.crm.domain.menu.enumset.MenuType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 메뉴
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "menu")
public class Menu extends UpdatedBaseEntity {

    /**
     * 메뉴 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    @Setter
    private Long id;

    /**
     * 상위 메뉴 시퀀스 (NULL: 최상위 메뉴)
     */
    @Column(name = "parent_menu_id")
    @Setter
    private Long parentId;

    /**
     * 그룹 시퀀스
     */
    @Column(name = "group_menu_id")
    @Setter
    private Long groupId;

    /**
     * 메뉴 깊이 (1부터 시작)
     */
    @NotNull
    @Column(name = "depth")
    private Integer depth;

    /**
     * 정렬 순서
     */
    @NotNull
    @Column(name = "sort_order")
    private Integer sortOrder;

    /**
     * 메뉴 이름
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "menu_name")
    private String name;

    /**
     * 메뉴 유형 [LINK 링크 | CONTENT 콘텐츠 | BOARD 게시판]
     */
    @NotNull
    @Column(name = "menu_type")
    private MenuType type;

    /**
     * 게시판 시퀀스 (메뉴 유형 BOARD 전용)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    /**
     * 콘텐츠 (메뉴 유형 CONTENT 전용)
     */
    @Lob
    @Column(name = "content")
    private String content;

    /**
     * 링크 URL (메뉴 유형 LINK 전용)
     */
    @Size(max = 255)
    @Column(name = "link_url")
    private String linkUrl;

    /**
     * 링크 타겟 (메뉴 유형 LINK 전용) [SELF 현재 창 | BLANK 새 창]
     */
    @Column(name = "link_target")
    private LinkTarget linkTarget;

    /**
     * SEO 최적화 키워드
     */
    @Size(max = 255)
    @Column(name = "seo_keyword")
    private String seoKeyword;

    /**
     * SEO 최적화 설명
     */
    @Size(max = 255)
    @Column(name = "seo_description")
    private String seoDescription;

    /**
     * 담당자 담당부서
     */
    @Size(max = 255)
    @Column(name = "manager_department")
    private String managerDepartment;

    /**
     * 담당자 연락처
     */
    @Size(max = 255)
    @Column(name = "manager_telephone")
    private String managerTelephone;

    /**
     * 담당자 이메일
     */
    @Size(max = 255)
    @Column(name = "manager_email")
    private String managerEmail;

    /**
     * 공공저작물 자유 이용 여부
     */
    @NotNull
    @Column(name = "public_use_yn", nullable = false, columnDefinition = "CHAR")
    private YN publicUseYn;

    /**
     * 노출 여부
     */
    @NotNull
    @Column(name = "view_yn", nullable = false, columnDefinition = "CHAR")
    private YN viewYn;

}
