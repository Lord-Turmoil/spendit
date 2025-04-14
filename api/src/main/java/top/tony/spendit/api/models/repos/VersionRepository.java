/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.models.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.tony.spendit.api.models.Version;

import java.util.List;
import java.util.Optional;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {
    Optional<Version> findFirstByOrderByTimestampDesc();

    List<Version> findAllByOrderByTimestampDesc();
}
