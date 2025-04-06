/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad Request")
public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        this("Bad Request");
    }

    public BadRequestException(String message) {
        super(message);
    }
}
