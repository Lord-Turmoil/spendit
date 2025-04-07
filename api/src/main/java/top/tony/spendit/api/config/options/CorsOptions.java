/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.config.options;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cors")
@Data
public class CorsOptions {
    private String[] allowedOrigins;
    private String[] allowedMethods;
    private boolean allowCredentials;
}

