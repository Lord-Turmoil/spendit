/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package top.tony.spendit.api.modules.auth.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.tony.spendit.api.extensions.cookies.ICookieSetter;
import top.tony.spendit.api.extensions.jwt.JwtIssueException;
import top.tony.spendit.api.extensions.jwt.JwtVerifyException;
import top.tony.spendit.api.modules.auth.config.AuthConfig;
import top.tony.spendit.api.modules.auth.models.AuthPayload;
import top.tony.spendit.api.modules.auth.services.AuthService;

@Component
@RequiredArgsConstructor
public class AuthApi {
    private final AuthService authService;
    private final ICookieSetter jwtCookieSetter;

    public String issueJwt(AuthPayload auth) throws JwtIssueException {
        return authService.issueJwt(auth);
    }

    public AuthPayload verifyJwt(String jwt) throws JwtVerifyException {
        return authService.verifyJwt(jwt);
    }

    public String issueRefresh(AuthPayload auth) throws JwtIssueException {
        return authService.issueRefresh(auth);
    }

    public Cookie setJwtCookie(String jwt) {
        return authService.setJwtCookie(jwt);
    }

    public Cookie cleanJwtCookie() {
        return authService.cleanJwtCookie();
    }

    public Cookie setRefreshCookie(String refresh) {
        return authService.setRefreshCookie(refresh);
    }

    public Cookie cleanRefreshCookie() {
        return authService.cleanRefreshCookie();
    }

    public String getJwt(HttpServletRequest request) {
        return jwtCookieSetter.get(request);
    }

    public AuthPayload getAuth(HttpServletRequest request) {
        return (AuthPayload) request.getAttribute(AuthConfig.AUTH_ATTR);
    }
}
