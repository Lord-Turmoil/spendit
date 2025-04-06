/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.extensions.jwt;

public class JwtIssueException extends Exception {
    public JwtIssueException(String message) {
        super(message);
    }

    public JwtIssueException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtIssueException(Throwable cause) {
        super(cause);
    }
}
