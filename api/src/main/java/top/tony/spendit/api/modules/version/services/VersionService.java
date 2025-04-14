/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.version.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tony.spendit.api.exceptions.NotFoundException;
import top.tony.spendit.api.models.Version;
import top.tony.spendit.api.models.repos.VersionRepository;
import top.tony.spendit.api.modules.version.dto.CreateVersionRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VersionService {
    private final VersionRepository versionRepository;

    public Version getLatest() {
        return versionRepository.findFirstByOrderByTimestampDesc()
                .orElseThrow(() -> new NotFoundException("No version found"));
    }

    public List<Version> list() {
        return versionRepository.findAllByOrderByTimestampDesc();
    }

    public Version create(CreateVersionRequest request) {
        Version version = new Version(request.getVersion(), request.getDownloadUrl(), request.getCode(), request.getDescription());
        return versionRepository.save(version);
    }

    public void withdraw(Long id) {
        Version version = versionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Version not found"));
        versionRepository.delete(version);
    }
}
