/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.config.options;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cookies")
@Data
public class CookiesOptions {
    public static final String JWT_COOKIE = "spendit-jwt";
    public static final String JWT_REFRESH_COOKIE = "spendit-refresh";

    public static final String PATH = "/";

    private String domain;
    private boolean secure;
    private boolean httpOnly;
}
