package top.tony.spendit.api.modules.account.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
