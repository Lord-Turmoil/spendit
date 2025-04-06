/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.tony.spendit.api.common.Globals;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String badge;

    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime createdAt;

    private boolean admin;

    public Account(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.badge = admin ? "Admin" : "User";
        this.createdAt = LocalDateTime.now();
        this.admin = admin;
    }
}
