package webbizz.crm.domain.authoritypattern.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.BaseEntity;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.member.enumset.MemberRole;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 권한 패턴
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "authority_pattern")
public class AuthorityPattern extends BaseEntity {

    /**
     * 권한 패턴 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_pattern_id", nullable = false)
    private Long id;

    /**
     * 상위 권한 패턴 시퀀스 (NULL: 최상위 권한 패턴)
     */
    @Column(name = "parent_authority_pattern_id")
    private Long parentId;

    /**
     * 권한 [ROLE_USER 회원 | ROLE_ADMIN 관리자]
     */
    @Column(name = "role", nullable = false)
    private MemberRole role;

    /**
     * 권한 이름
     */
    @Column(name = "authority_name", nullable = false)
    private String name;

    /**
     * URL 패턴
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "url_pattern", nullable = false)
    private String urlPattern;

    /**
     * 단계 [1 상단 메뉴 | 2 사이드 메뉴 | NULL 분류 없음]
     */
    @Column(name = "depth")
    private Integer depth;

    /**
     * 정렬 순서
     */
    @Column(name = "sort_order")
    private Integer sortOrder;

    /**
     * 설명 (관리자 메모용)
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * 관리 여부 (메뉴 권한에 표시 여부)
     */
    @NotNull
    @Column(name = "manage_yn", nullable = false, columnDefinition = "CHAR")
    private YN manageYn;

}
