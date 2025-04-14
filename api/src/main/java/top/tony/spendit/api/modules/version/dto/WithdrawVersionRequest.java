/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.version.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WithdrawVersionRequest {
    @NotNull
    private Long id;
}
