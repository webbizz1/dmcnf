package webbizz.crm.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Entity 에서 Soft Delete 처리가 필요한 경우 사용하는 추상 클래스
 *
 * @since 2024-11-20
 * @author TAEROK HWANG
 * @version 1.0.1
 */
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public abstract class SoftBaseEntity extends BaseEntity {

    /** 삭제 여부 */
    @Column(nullable = false, insertable = false, columnDefinition = "CHAR")
    @Builder.Default
    protected YN delYn = YN.N;

}
