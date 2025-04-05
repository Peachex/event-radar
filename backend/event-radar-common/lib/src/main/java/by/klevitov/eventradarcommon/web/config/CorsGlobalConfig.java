package by.klevitov.eventradarcommon.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
public class CorsGlobalConfig implements WebMvcConfigurer {
    private static final String ALL_REQUESTS_PATH_PATTERN = "/**";
    private static final String ALLOWED_ORIGINS_SPLITTER_REGEX = "[,;]";

    @Value("${cors.allowed.origins:}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(ALL_REQUESTS_PATH_PATTERN)
                .allowedOrigins(allowedOrigins.split(ALLOWED_ORIGINS_SPLITTER_REGEX))
                .allowedMethods(GET.name(), POST.name(), PUT.name(), DELETE.name(), OPTIONS.name())
                .allowCredentials(true);
    }
}
