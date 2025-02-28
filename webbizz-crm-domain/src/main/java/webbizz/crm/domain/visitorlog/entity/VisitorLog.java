package webbizz.crm.domain.visitorlog.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.member.entity.Member;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 방문자 로그
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "visitor_log")
public class VisitorLog {

    /**
     * 방문자 로그 시퀀스
     */
    @Id
    @Column(name = "visitor_log_id")
    private Long id;

    /**
     * 애플리케이션 이름
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "application_name")
    private String applicationName;

    /**
     * 회원
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 방문 IP
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "remote_addr")
    private String remoteAddr;

    /**
     * 방문 URI
     */
    @NotNull
    @Size(max = 511)
    @Column(name = "request_uri", length = 511)
    private String requestUri;

    /**
     * 방문 수
     */
    @NotNull
    @Column(name = "hits")
    private Long hits;

    /**
     * 그룹 날짜
     */
    @Column(nullable = false, updatable = false)
    protected LocalDate groupDate;

    /**
     * 등록 일시
     */
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    /**
     * 수정 일시
     */
    @Column(insertable = false)
    protected LocalDateTime updatedAt;

}
