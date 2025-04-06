/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.invitation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InvokeInvitationRequest {
    @NotNull
    private Long id;
}
