package webbizz.crm.domain.attachment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import webbizz.crm.converter.JsonConverter;
import webbizz.crm.converter.JsonListConverter;
import webbizz.crm.util.PatternUtils;

import javax.persistence.Converter;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AttachmentDto {

    private Long id;

    private String uuid;

    private String uploadName;

    private String originalName;

    private String extension;

    private String path;

    private String realUrl;

    private Long size;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternUtils.DATE_TIME_MILLS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;


    @Converter(autoApply = true)
    public static class AttachmentDtoConverter extends JsonConverter<AttachmentDto> {}

    @Converter(autoApply = true)
    public static class AttachmentDtoListConverter extends JsonListConverter<AttachmentDto> {}

}
