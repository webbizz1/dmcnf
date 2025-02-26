package webbizz.crm.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "webbizz.attachment")
@Validated
@Getter
public class AttachmentProperties {

    /**
     * 첨부파일 저장 경로
     */
    private final String path;

    /**
     * 업로드 가능한 파일 확장자 (콤마로 분리)
     */
    private final String[] allowExtensions;

    /**
     * 첨부파일 최대 크기 (spring.servlet.multipart.max-request-size, spring.servlet.multipart.max-file-size 보다 작아야 함)
     */
    private final DataSize maxFileSize;

    /**
     * 서비스 도메인
     */
    @NotBlank
    private final String serviceDomain;

    @ConstructorBinding
    public AttachmentProperties(@DefaultValue({
                                        "gif", "jpg", "jpeg", "png", "webp",
                                        "doc", "docx", "ppt", "pptx", "xls", "xlsx", "pdf", "hwp", "txt"
                                }) String[] allowExtensions,
                                @DefaultValue("10MB") DataSize maxFileSize,
                                String path,
                                String serviceDomain) {

        if (path == null || path.isEmpty()) {
            path = System.getProperties().getProperty("user.dir") + "/uploads";
        }

        this.allowExtensions = allowExtensions;
        this.maxFileSize = maxFileSize;
        this.path = path;
        this.serviceDomain = serviceDomain;
    }

}
