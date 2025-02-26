package webbizz.crm.domain.memberauditor.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.BaseEntity;
import webbizz.crm.domain.member.entity.Member;
import webbizz.crm.domain.member.enumset.MemberAuditorType;

import javax.persistence.*;

/**
 * 회원 이력
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "member_auditor")
public class MemberAuditor extends BaseEntity {

    /**
     * 회원 이력 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_auditor_id")
    private Long id;

    /**
     * 회원
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 대상 회원
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_member_id")
    private Member targetMember;

    /**
     * 이벤트 이름
     */
    @Column(name = "event_name")
    private MemberAuditorType type;

    /**
     * 발생 IP
     */
    @Column(name = "remote_addr")
    private String remoteAddr;

}
