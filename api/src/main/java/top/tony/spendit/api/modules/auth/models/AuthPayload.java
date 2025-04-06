/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.auth.models;

import lombok.Data;

@Data
public class AuthPayload {
    private Long id;
    private String username;
    private boolean admin;
}
