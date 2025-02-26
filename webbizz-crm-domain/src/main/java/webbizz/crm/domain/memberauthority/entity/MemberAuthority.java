package webbizz.crm.domain.memberauthority.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.BaseEntity;
import webbizz.crm.domain.authoritypattern.entity.AuthorityPattern;
import webbizz.crm.domain.member.entity.Member;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 회원 권한
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "member_authority")
public class MemberAuthority extends BaseEntity {

    /**
     * 회원 권한 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_authority_id", nullable = false)
    private Long id;

    /**
     * 회원
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * 권한 패턴
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "authority_pattern_id", nullable = false)
    private AuthorityPattern authorityPattern;

}
