/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.account.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import top.tony.spendit.api.aspect.IRequireValidation;

@Data
public class RegisterRequest implements IRequireValidation {
    @NotNull
    @Size(min = 1, max = 16)
    private String code;

    @NotNull
    @Size(min = 1, max = 16)
    private String username;

    @NotNull
    @Size(min = 1, max = 36)
    private String password;

    @Override
    public void validate() {
        // TODO: user regex to validate username and password
    }
}
