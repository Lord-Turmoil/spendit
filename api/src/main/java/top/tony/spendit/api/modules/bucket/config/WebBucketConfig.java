/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.bucket.config;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebBucketConfig implements WebMvcConfigurer {
    private final BucketOptions bucketOptions;

    @Override
    public void addResourceHandlers(@Nonnull ResourceHandlerRegistry registry) {
        if (bucketOptions.isServe()) {
            registry.addResourceHandler("/public/**")
                    .addResourceLocations("file:" + bucketOptions.getPublicRoot());
        }
    }
}
