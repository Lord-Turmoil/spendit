/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Forbidden")
public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        this("Forbidden");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
