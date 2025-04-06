/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.config;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.tony.spendit.api.config.options.CorsOptions;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebConfig implements WebMvcConfigurer {
    private final CorsOptions corsOptions;

    @Value("${springdoc.swagger-ui.enabled}")
    private boolean swaggerEnabled;

    @Override
    public void addResourceHandlers(@Nonnull ResourceHandlerRegistry registry) {
        if (swaggerEnabled) {
            log.warn("Adding swagger-ui resource handler");
            registry.addResourceHandler("/swagger-ui/swagger-ui.css")
                    .addResourceLocations("classpath:/static/")
                    .resourceChain(false);
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods(corsOptions.getAllowedMethods())
                .allowedHeaders(corsOptions.getAllowedHeaders())
                .allowedOrigins(corsOptions.getAllowedOrigins())
                .allowCredentials(corsOptions.isAllowCredentials())
                .maxAge(3600);
    }
}
