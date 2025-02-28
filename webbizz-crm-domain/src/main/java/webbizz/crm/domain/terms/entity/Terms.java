package webbizz.crm.domain.terms.entity;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import webbizz.crm.domain.UpdatedBaseEntity;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.terms.enumset.TermsType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 약관
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "terms")
public class Terms extends UpdatedBaseEntity {

    /**
     * 약관 시퀀스
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_id")
    private Long id;

    /**
     * 약관 유형 [TERMS 이용약관 | PRIVACY 개인정보처리방침]
     */
    @NotNull
    @Column(name = "terms_type")
    private TermsType type;

    /**
     * 내용
     */
    @NotNull
    @Lob
    @Column(name = "content")
    private String content;

    /**
     * 활성화 여부
     */
    @NotNull
    @Column(name = "active_yn", columnDefinition = "CHAR(1)")
    @Builder.Default
    private YN activeYn = YN.Y;

}
