package webbizz.crm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import webbizz.crm.interceptor.MenuInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final MenuInterceptor menuInterceptor;
    private final LocaleChangeInterceptor localeChangeInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(menuInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/images/**", "/js/**", "/pdf.js/**", "/api/**");

        registry.addInterceptor(localeChangeInterceptor)
                .addPathPatterns("/");
    }

}
