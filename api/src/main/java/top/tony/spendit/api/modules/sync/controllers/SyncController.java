/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.sync.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.tony.spendit.api.common.dto.DataResponse;
import top.tony.spendit.api.common.dto.MessageResponse;
import top.tony.spendit.api.modules.auth.aspect.ValidatePermission;
import top.tony.spendit.api.modules.auth.models.AuthPayload;
import top.tony.spendit.api.modules.sync.dto.*;
import top.tony.spendit.api.modules.sync.models.EntryMergeHint;
import top.tony.spendit.api.modules.sync.models.EntryTableJson;
import top.tony.spendit.api.modules.sync.models.TagJson;
import top.tony.spendit.api.modules.sync.services.EntrySyncService;
import top.tony.spendit.api.modules.sync.services.TagSyncService;

import java.util.List;

@RestController
@RequestMapping("api/sync")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Synchronization")
public class SyncController {
    private final TagSyncService tagSyncService;
    private final EntrySyncService entrySyncService;

    @PostMapping("tags/push")
    @Operation(summary = "Push tags to server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tags pushed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @ValidatePermission
    public MessageResponse pushTags(
            @RequestBody @Valid PushTagRequest request,
            AuthPayload auth
    ) {
        tagSyncService.push(String.valueOf(auth.getId()), request.getTags());
        return MessageResponse.ok("Tags pushed successfully");
    }

    @GetMapping("tags/pull")
    @Operation(summary = "Pull tags from server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tags pulled successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @ValidatePermission
    public DataResponse<TagJson> pullTags(
            AuthPayload auth
    ) {
        TagJson tags = tagSyncService.pull(String.valueOf(auth.getId()));
        return DataResponse.ok(tags);
    }

    @PostMapping("tags/merge")
    @Operation(summary = "Merge tags with server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tags merged successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @ValidatePermission
    public DataResponse<TagJson> mergeTags(
            @RequestBody @Valid PushTagRequest request,
            AuthPayload auth
    ) {
        TagJson tags = tagSyncService.merge(String.valueOf(auth.getId()), request.getTags());
        return DataResponse.ok(tags);
    }

    @PostMapping("entry/push")
    @Operation(summary = "Push entries to server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entries pushed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @ValidatePermission
    public MessageResponse pushEntries(
            @RequestBody @Valid PushEntryRequest request,
            AuthPayload auth
    ) {
        entrySyncService.push(String.valueOf(auth.getId()), request.getEntries());
        return MessageResponse.ok("Entries pushed successfully");
    }

    @GetMapping("entry/pull")
    @Operation(summary = "Pull entries from server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entries pulled successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @ValidatePermission
    public DataResponse<PullEntryResponse> pullEntries(
            AuthPayload auth
    ) {
        List<EntryTableJson> entries = entrySyncService.pull(String.valueOf(auth.getId()));
        return DataResponse.ok(new PullEntryResponse(entries));
    }

    @PostMapping("entry/merge/hint")
    @Operation(summary = "Get merge hint for entries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Merge hint retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @ValidatePermission
    public DataResponse<EntryMergeHint> getMergeHint(
            @RequestBody @Valid MergeEntryHintRequest request,
            AuthPayload auth
    ) {
        EntryMergeHint hint = entrySyncService.getMergeHint(String.valueOf(auth.getId()), request.getMeta());
        return DataResponse.ok(hint);
    }

    @PostMapping("entry/merge")
    @Operation(summary = "Merge entries with server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entries merged successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @ValidatePermission
    public DataResponse<MergeEntryResponse> mergeEntries(
            @RequestBody @Valid MergeEntryRequest request,
            AuthPayload auth
    ) {
        List<EntryTableJson> entries = entrySyncService.merge(
                String.valueOf(auth.getId()),
                request
        );
        return DataResponse.ok(new MergeEntryResponse(request.getHint(), entries));
    }
}
