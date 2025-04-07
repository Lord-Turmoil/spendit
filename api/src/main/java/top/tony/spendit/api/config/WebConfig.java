/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.tony.spendit.api.config.options.CorsOptions;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebConfig implements WebMvcConfigurer {
    private final CorsOptions corsOptions;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        for (String origin : corsOptions.getAllowedOrigins()) {
            log.warn("CORS allowed origin: {}", origin);
        }
        log.warn("CORS allowed methods: {}", String.join(", ", corsOptions.getAllowedMethods()));

        registry.addMapping("/**")
                .allowedOrigins(corsOptions.getAllowedOrigins())
                .allowedMethods(corsOptions.getAllowedMethods())
                .allowCredentials(corsOptions.isAllowCredentials())
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
