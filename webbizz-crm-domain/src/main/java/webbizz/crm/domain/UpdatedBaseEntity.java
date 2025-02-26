package webbizz.crm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import webbizz.crm.util.PatternUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.LocalDateTime;

/**
 * Entity 에서 수정자 정보가 필요한 경우 사용하는 추상 클래스
 *
 * @see BaseEntity
 * @since 2024-09-25
 * @author TAEROK HWANG
 * @version 1.0.0
 */
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class UpdatedBaseEntity extends SoftBaseEntity {

    /**
     * 버전
     */
    @Version
    @Getter(AccessLevel.NONE)
    protected Integer version;

    /** 수정자 아이디 */
    @LastModifiedBy
    @Column(insertable = false)
    protected String updatedId;

    /** 수정 일시 */
    @LastModifiedDate
    @Column(insertable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternUtils.DATE_TIME_MILLS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    protected LocalDateTime updatedAt;

}
