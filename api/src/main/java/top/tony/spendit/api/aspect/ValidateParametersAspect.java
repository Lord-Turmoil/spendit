/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package top.tony.spendit.api.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(100)
@RequiredArgsConstructor
@Slf4j
public class ValidateParametersAspect {
    @Before("@annotation(ValidateParameters)")
    public void intercept(final JoinPoint point) {
        Object[] args = point.getArgs();
        for (Object arg : args) {
            if (arg instanceof IRequireValidation validation) {
                validation.validate();
            }
        }
    }
}
