/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatePagination {
    int basePage() default 1;

    int maxPage() default 1000;

    int minPageSize() default 1;

    int maxPageSize() default 1000;
}
