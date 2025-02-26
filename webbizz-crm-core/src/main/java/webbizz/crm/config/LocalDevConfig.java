package webbizz.crm.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.time.Duration;

/**
 * 로컬 환경에서 리소스 파일을 즉시 반영하기 위한 설정
 *
 * @author TAEROK HWANG
 */
@Configuration
@Profile("local")
public class LocalDevConfig implements WebMvcConfigurer {

//    /**
//     * Thymeleaf 템플릿 파일을 즉시 반영하기 위한 설정 <br />
//     * 이미 로드되어 있는 Thymeleaf 템플릿 엔진이 필요하므로 생성자에서 Dependency Injection 사용하여 주입
//     *
//     * @param templateEngine Thymeleaf 템플릿 엔진
//     */
//    @SneakyThrows
//    public LocalDevConfig(final TemplateEngine templateEngine) {
//        final String fileSeparator = File.separatorChar == '\\' ? "\\\\" : File.separator;
//        final ClassPathResource applicationProperties = new ClassPathResource("application.properties");
//        if (applicationProperties.isFile()) {
//            File sourceRoot = applicationProperties.getFile().getParentFile();
//            final FileTemplateResolver fileTemplateResolver = new FileTemplateResolver();
//            fileTemplateResolver.setPrefix(sourceRoot.getPath().replace(
//                    "build|resources|main".replaceAll("\\|", fileSeparator),
//                    "src|main|resources|templates|".replaceAll("\\|", fileSeparator)));
//            fileTemplateResolver.setSuffix(".html");
//            fileTemplateResolver.setCacheable(false);
//            fileTemplateResolver.setCharacterEncoding("UTF-8");
//            fileTemplateResolver.setCheckExistence(true);
//            templateEngine.setTemplateResolver(fileTemplateResolver);
//        }
//    }

    /**
     * 정적 리소스 파일을 즉시 반영하기 위한 설정
     *
     * @param registry 정적 리소스 파일을 등록할 ResourceHandlerRegistry
     */
    @Override
    @SneakyThrows
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        final String fileSeparator = File.separatorChar == '\\' ? "\\\\" : File.separator;
        final ClassPathResource applicationProperties = new ClassPathResource("application.properties");
        if (applicationProperties.isFile()) {
            File sourceRoot = applicationProperties.getFile().getParentFile();
            registry.addResourceHandler("/**")
                    .addResourceLocations("file:" + sourceRoot.getPath().replace(
                            "build|resources|main".replaceAll("\\|", fileSeparator),
                            "src|main|resources|static|".replaceAll("\\|", fileSeparator)));
            registry.addResourceHandler("/webfonts/**")
                    .addResourceLocations("classpath:/static/webfonts/")
                    .setCacheControl(CacheControl.maxAge(Duration.ofHours(1)));
        }
    }

}
