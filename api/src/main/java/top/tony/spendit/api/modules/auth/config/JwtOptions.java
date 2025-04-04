/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package top.tony.spendit.api.modules.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtOptions {
    private String secret;
    private int jwtExpiration;
    private int refreshExpiration;
}
