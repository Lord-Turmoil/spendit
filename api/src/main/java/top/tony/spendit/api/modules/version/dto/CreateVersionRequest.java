/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.version.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateVersionRequest {
    @NotNull
    private String version;

    @NotNull
    private String downloadUrl;

    @NotNull
    private String code;

    @NotNull
    private String description;
}
