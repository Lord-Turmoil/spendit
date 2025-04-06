/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Server Error")
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException() {
        this("Internal Server Error");
    }

    public InternalServerErrorException(String message) {
        super(message);
    }
}
