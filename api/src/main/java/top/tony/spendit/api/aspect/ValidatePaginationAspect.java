/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package top.tony.spendit.api.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.tony.spendit.api.exceptions.BadRequestException;

import java.lang.reflect.Method;

@Component
@Aspect
@Order(101)
@RequiredArgsConstructor
public class ValidatePaginationAspect {
    @Before("@annotation(ValidatePagination)")
    public void intercept(final JoinPoint point) {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        ValidatePagination rule = method.getAnnotation(ValidatePagination.class);
        for (int i = 0; i < method.getParameterCount(); i++) {
            if (method.getParameters()[i].isAnnotationPresent(Page.class)) {
                validatePage((int) args[i], rule);
            } else if (method.getParameters()[i].isAnnotationPresent(PageSize.class)) {
                validatePageSize((int) args[i], rule);
            }
        }
    }

    private void validatePage(int page, ValidatePagination rule) {
        if (page < rule.basePage() || page > rule.maxPage()) {
            throw new BadRequestException("Invalid page number");
        }
    }

    private void validatePageSize(int pageSize, ValidatePagination rule) {
        if (pageSize < rule.minPageSize() || pageSize > rule.maxPageSize()) {
            throw new BadRequestException("Invalid page size");
        }
    }
}
