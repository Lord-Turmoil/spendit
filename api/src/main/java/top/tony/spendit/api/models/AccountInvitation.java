/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class AccountInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long accountId;
    private Long invitationId;

    public AccountInvitation(Long accountId, Long invitationId) {
        this.accountId = accountId;
        this.invitationId = invitationId;
    }
}
