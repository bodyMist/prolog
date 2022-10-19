package kit.prolog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:static/file-server.properties")
public class WebClientConfig {
    @Value("${file.server.ip}")
    private String FILE_SERVER_IP;
    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(FILE_SERVER_IP)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                .build();
    }
}
