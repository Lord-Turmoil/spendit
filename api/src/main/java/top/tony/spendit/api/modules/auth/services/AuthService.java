/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.auth.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.tony.spendit.api.common.requets.BaseService;
import top.tony.spendit.api.extensions.cookies.ICookieSetter;
import top.tony.spendit.api.extensions.jwt.IJwtIssuer;
import top.tony.spendit.api.extensions.jwt.JwtIssueException;
import top.tony.spendit.api.extensions.jwt.JwtVerifyException;
import top.tony.spendit.api.modules.auth.models.AuthPayload;

@Service
@RequiredArgsConstructor
public class AuthService extends BaseService {
    private final IJwtIssuer jwtIssuer;
    private final IJwtIssuer refreshIssuer;
    private final ICookieSetter jwtCookieSetter;
    private final ICookieSetter refreshCookieSetter;

    public String issueJwt(AuthPayload auth) throws JwtIssueException {
        String subject;
        try {
            subject = mappers.toJson(auth);
        } catch (JsonProcessingException e) {
            throw new JwtIssueException("Failed to serialize auth view", e);
        }

        return jwtIssuer.issue(subject);
    }

    public AuthPayload verifyJwt(String jwt) throws JwtVerifyException {
        return validateJwt(jwtIssuer, jwt);
    }

    public String issueRefresh(AuthPayload auth) throws JwtIssueException {
        String subject;
        try {
            subject = mappers.toJson(auth);
        } catch (JsonProcessingException e) {
            throw new JwtIssueException("Failed to serialize auth view", e);
        }
        return refreshIssuer.issue(subject);
    }

    public AuthPayload verifyRefresh(String refresh) throws JwtVerifyException {
        return validateJwt(refreshIssuer, refresh);
    }

    public Cookie setJwtCookie(String jwt) {
        return jwtCookieSetter.set(jwt);
    }

    public Cookie cleanJwtCookie() {
        return jwtCookieSetter.clean();
    }

    public Cookie setRefreshCookie(String refresh) {
        return refreshCookieSetter.set(refresh);
    }

    public Cookie cleanRefreshCookie() {
        return refreshCookieSetter.clean();
    }

    private AuthPayload validateJwt(IJwtIssuer issuer, String jwt) throws JwtVerifyException {
        if (jwt == null) {
            throw new JwtVerifyException("JWT is null");
        }

        String subject = issuer.verify(jwt);
        try {
            return mappers.fromJson(subject, AuthPayload.class);
        } catch (JsonProcessingException e) {
            throw new JwtVerifyException("Failed to deserialize auth view", e);
        }
    }
}
