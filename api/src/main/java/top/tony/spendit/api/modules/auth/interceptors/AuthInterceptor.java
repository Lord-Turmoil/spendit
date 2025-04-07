/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.auth.interceptors;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.tony.spendit.api.exceptions.InternalServerErrorException;
import top.tony.spendit.api.exceptions.UnauthorizedException;
import top.tony.spendit.api.extensions.cookies.ICookieSetter;
import top.tony.spendit.api.extensions.jwt.JwtIssueException;
import top.tony.spendit.api.extensions.jwt.JwtVerifyException;
import top.tony.spendit.api.modules.auth.config.AuthConfig;
import top.tony.spendit.api.modules.auth.models.AuthPayload;
import top.tony.spendit.api.modules.auth.services.AuthService;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    private final ICookieSetter jwtCookieSetter;
    private final ICookieSetter refreshCookieSetter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Always allow preflight requests.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String jwt = jwtCookieSetter.get(request);
        AuthPayload payload;

        // Validate JWT first.
        try {
            payload = authService.verifyJwt(jwt);
            request.setAttribute(AuthConfig.AUTH_ATTR, payload);
            return true;
        } catch (JwtVerifyException e) {
            // JWT expired, try refresh token.
        }

        // Validate refresh token second.
        try {
            payload = authService.verifyRefresh(refreshCookieSetter.get(request));
            String newJwt = authService.issueJwt(payload);
            String newRefresh = authService.issueRefresh(payload);
            Cookie jwtCookie = jwtCookieSetter.set(newJwt);
            Cookie refreshCookie = refreshCookieSetter.set(newRefresh);
            response.addCookie(jwtCookie);
            response.addCookie(refreshCookie);
        } catch (JwtVerifyException e) {
            throw new UnauthorizedException("用户凭证失效，请重新登录");
        } catch (JwtIssueException e) {
            log.error("Failed to issue new JWT: {}", e.getMessage());
            throw new InternalServerErrorException("Failed to issue new JWT");
        }

        request.setAttribute(AuthConfig.AUTH_ATTR, payload);

        return true;
    }
}
