/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.auth.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatePermission {
    AuthLevel value() default AuthLevel.USER;
}
