package webbizz.crm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@SuppressWarnings({"DuplicatedCode", "LoggingSimilarMessage"})
public class RestTemplateConfig implements ClientHttpRequestInterceptor {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();

        // 응답 본문을 여러 번 읽을 수 있도록 BufferingClientHttpRequestFactory 를 사용
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        // 요청과 응답을 로깅하기 위한 인터셉터 추가
        restTemplate.getInterceptors().add(this);

        return restTemplate;
    }

    @NonNull
    @Override
    public ClientHttpResponse intercept(@NonNull HttpRequest request,
                                        @NonNull byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {
        log.info("==================== REST TEMPLATE ====================");
        log.info("{} {}", request.getMethod(), request.getURI());
        log.info("[Headers]");
        request.getHeaders().forEach((key, value) -> log.info("  · {}: {}", key, value));
        if (body.length > 0) {
            log.info("[Body]");
            log.info("  · {}", new String(body));
        }
        log.info("-----------------------------------------------------");
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        log.info("{}", response.getStatusCode());
        log.info("[Headers]");
        response.getHeaders().forEach((key, value) -> log.info("  · {}: {}", key, value));
        log.info("[Body]");
        String body = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        log.info("  · {}", body.replaceAll("\n", ""));
        log.info("==================== REST TEMPLATE ====================");
    }

}
