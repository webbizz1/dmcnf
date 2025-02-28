package webbizz.crm.domain.member.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.converter.JsonListStringConverter;
import webbizz.crm.domain.UpdatedBaseEntity;
import webbizz.crm.domain.member.enumset.MemberRole;
import webbizz.crm.domain.member.enumset.MemberStatus;
import webbizz.crm.domain.memberauthority.entity.MemberAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 회원
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "member")
public class Member extends UpdatedBaseEntity {

    /**
     * 회원 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    /**
     * 로그인 아이디
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "login_id")
    private String loginId;

    /**
     * 비밀번호
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "password")
    private String password;

    /**
     * 회원 권한 [ROLE_USER 회원 | ROLE_ADMIN 관리자]
     */
    @Column(name = "role")
    private MemberRole role;

    /**
     * 회원 상태 [NORMAL 정상 | LOCKED 잠김 | SUSPENDED 정지 | WITHDRAWAL 탈퇴]
     */
    @Column(name = "status_code")
    private MemberStatus status;

    /**
     * 이름
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "real_name")
    private String realName;

    /**
     * 이메일
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "email")
    private String email;

    /**
     * 연락처
     */
    @Size(max = 255)
    @Column(name = "telephone_number")
    private String telephoneNumber;

    /**
     * 휴대폰 번호
     */
    @Size(max = 255)
    @Column(name = "mobile_number")
    private String mobileNumber;

    /**
     * 로그인 허용 IP 주소 JSON
     */
    @Lob
    @Column(name = "allow_ip_addresses_json", columnDefinition = "TEXT")
    @Convert(converter = JsonListStringConverter.class)
    private List<String> allowIpAddresses;

    /**
     * 마지막 로그인 일시
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /**
     * 탈퇴 일시
     */
    @Column(name = "withdrawal_at")
    private LocalDateTime withdrawalAt;

    /**
     * 탈퇴 사유
     */
    @Lob
    @Column(name = "withdrawal_reason", columnDefinition = "TEXT")
    private String withdrawalReason;


    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MemberAuthority> authorities;

    public void setAuthorities(final List<MemberAuthority> authorities) {
        this.authorities = authorities;
        authorities.forEach(authority -> authority.setMember(this));
    }

}
