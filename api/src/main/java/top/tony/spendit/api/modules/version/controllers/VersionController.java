/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.version.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.tony.spendit.api.common.dto.DataResponse;
import top.tony.spendit.api.common.dto.MessageResponse;
import top.tony.spendit.api.models.Version;
import top.tony.spendit.api.modules.auth.aspect.AuthLevel;
import top.tony.spendit.api.modules.auth.aspect.ValidatePermission;
import top.tony.spendit.api.modules.version.dto.CreateVersionRequest;
import top.tony.spendit.api.modules.version.dto.WithdrawVersionRequest;
import top.tony.spendit.api.modules.version.services.VersionService;

import java.util.List;

@RestController
@RequestMapping("api/version")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Version")
public class VersionController {
    private final VersionService versionService;

    @PostMapping("publish")
    @Operation(summary = "Publish a new version")
    @ValidatePermission(AuthLevel.ADMIN)
    public DataResponse<Version> publish(
            @RequestBody @Valid CreateVersionRequest request
    ) {
        Version version = versionService.create(request);
        return DataResponse.ok(version);
    }

    @PostMapping("withdraw")
    @Operation(summary = "Withdraw a version")
    @ValidatePermission(AuthLevel.ADMIN)
    public MessageResponse withdraw(
            @RequestBody @Valid WithdrawVersionRequest request
    ) {
        versionService.withdraw(request.getId());
        return MessageResponse.ok("Version withdrawn");
    }

    @GetMapping("list")
    @Operation(summary = "List all versions")
    @ValidatePermission(AuthLevel.ADMIN)
    public DataResponse<List<Version>> list() {
        return DataResponse.ok(versionService.list());
    }

    @GetMapping("latest")
    @Operation(summary = "Get the latest version")
    public DataResponse<Version> getLatest() {
        return DataResponse.ok(versionService.getLatest());
    }
}
