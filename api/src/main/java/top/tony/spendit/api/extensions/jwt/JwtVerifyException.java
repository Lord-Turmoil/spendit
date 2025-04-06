/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.extensions.jwt;

public class JwtVerifyException extends Exception {
    public JwtVerifyException(String message) {
        super(message);
    }

    public JwtVerifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtVerifyException(Throwable cause) {
        super(cause);
    }
}
