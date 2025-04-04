/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package top.tony.spendit.api.modules.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.tony.spendit.api.config.options.CookiesOptions;
import top.tony.spendit.api.extensions.cookies.CookieSetter;
import top.tony.spendit.api.extensions.cookies.ICookieSetter;
import top.tony.spendit.api.extensions.jwt.IJwtIssuer;
import top.tony.spendit.api.extensions.jwt.JwtIssuer;

@Component
@RequiredArgsConstructor
public class AuthConfig {
    public static final String AUTH_ATTR = "auth";

    private final JwtOptions jwtOptions;
    private final CookiesOptions cookiesOptions;

    @Bean("jwtIssuer")
    public IJwtIssuer getJwtIssuer() {
        return new JwtIssuer(jwtOptions.getJwtExpiration() * 1000, jwtOptions.getSecret());
    }

    @Bean("refreshIssuer")
    public IJwtIssuer getRefreshIssuer() {
        return new JwtIssuer(jwtOptions.getRefreshExpiration() * 1000, jwtOptions.getSecret());
    }

    @Bean("jwtCookieSetter")
    public ICookieSetter getJwtCookieSetter() {
        return new CookieSetter(
                CookiesOptions.JWT_COOKIE,
                cookiesOptions.getDomain(),
                CookiesOptions.PATH,
                jwtOptions.getJwtExpiration(),
                cookiesOptions.isHttpOnly(),
                cookiesOptions.isSecure());
    }

    @Bean("refreshCookieSetter")
    public ICookieSetter getRefreshCookieSetter() {
        return new CookieSetter(
                CookiesOptions.JWT_REFRESH_COOKIE,
                cookiesOptions.getDomain(),
                CookiesOptions.PATH,
                jwtOptions.getRefreshExpiration(),
                cookiesOptions.isHttpOnly(),
                cookiesOptions.isSecure());
    }
}
