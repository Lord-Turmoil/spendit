/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Found")
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        this("Not Found");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
