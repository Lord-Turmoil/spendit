/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Unauthorized Request")
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        this("Unauthorized Request");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
