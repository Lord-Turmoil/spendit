/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.auth.aspect;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.tony.spendit.api.common.utils.Requests;
import top.tony.spendit.api.exceptions.ForbiddenException;
import top.tony.spendit.api.exceptions.UnauthorizedException;
import top.tony.spendit.api.modules.auth.api.AuthApi;
import top.tony.spendit.api.modules.auth.models.AuthPayload;

import java.lang.reflect.Method;

/**
 * It injects {@link top.tony.spendit.api.modules.auth.models.AuthPayload} object into the method arguments.
 */
@Component
@Aspect
@Order(200)
@RequiredArgsConstructor
@Slf4j
public class ValidatePermissionAspect {
    private final AuthApi authApi;

    @Around("@annotation(ValidatePermission)")
    public Object intercept(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        ValidatePermission permission = method.getAnnotation(ValidatePermission.class);

        AuthPayload payload = validatePermission(method, permission.value());
        if (payload != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof AuthPayload) {
                    args[i] = payload;
                    break;
                }
            }
        }

        return point.proceed(args);
    }

    private AuthPayload validatePermission(Method method, AuthLevel level) {
        if (level == AuthLevel.ALL) {
            return null;
        }
        HttpServletRequest request = Requests.getCurrentRequest();
        AuthPayload auth = authApi.getAuth(request);
        if (auth == null) {
            log.error("Require authentication for method {}", method.getName());
            throw new UnauthorizedException("Not logged in");
        } else if ((level == AuthLevel.ADMIN) && !auth.isAdmin()) {
            log.error("User {}({}) require admin permission for method {}", auth.getUsername(), auth.getId(), method.getName());
            throw new ForbiddenException("Permission denied");
        }
        return auth;
    }
}
