/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.models.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.tony.spendit.api.models.Invitation;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Invitation findByCodeEquals(String code);
}
