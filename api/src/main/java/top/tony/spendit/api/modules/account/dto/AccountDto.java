package top.tony.spendit.api.modules.account.dto;

import lombok.Data;

@Data
public class AccountDto {
    private Long id;
    private String username;
    private String badge;
    private boolean admin;
}
