/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package top.tony.spendit.api.modules.auth.models;

import lombok.Data;

@Data
public class AuthPayload {
    private Long id;
    private String username;
    private boolean admin;
}
