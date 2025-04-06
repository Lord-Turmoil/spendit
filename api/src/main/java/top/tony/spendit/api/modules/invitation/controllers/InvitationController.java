/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.invitation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.tony.spendit.api.aspect.Page;
import top.tony.spendit.api.aspect.PageSize;
import top.tony.spendit.api.aspect.ValidatePagination;
import top.tony.spendit.api.common.dto.DataResponse;
import top.tony.spendit.api.common.dto.MessageResponse;
import top.tony.spendit.api.common.dto.PageListDto;
import top.tony.spendit.api.common.requets.BaseController;
import top.tony.spendit.api.models.Invitation;
import top.tony.spendit.api.modules.auth.aspect.AuthLevel;
import top.tony.spendit.api.modules.auth.aspect.ValidatePermission;
import top.tony.spendit.api.modules.invitation.dto.CreateInvitationRequest;
import top.tony.spendit.api.modules.invitation.dto.InvokeInvitationRequest;
import top.tony.spendit.api.modules.invitation.dto.ListInvitationRequest;
import top.tony.spendit.api.modules.invitation.services.InvitationService;

@RestController
@RequestMapping("api/admin/invitation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Invitation Admin")
public class InvitationController extends BaseController {
    private final InvitationService invitationService;

    @PostMapping("create")
    @Operation(summary = "Create an invitation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invitation created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Permission denied"),
    })
    @ValidatePermission(AuthLevel.ADMIN)
    public DataResponse<Invitation> create(
            @RequestBody @Valid CreateInvitationRequest request
    ) {
        Invitation invitation = invitationService.create(request);
        return DataResponse.ok("Invitation created", invitation);
    }

    @PostMapping("invoke")
    @Operation(summary = "Invoke an invitation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invitation invoked successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Permission denied"),
    })
    @ValidatePermission(AuthLevel.ADMIN)
    public MessageResponse invoke(
            @RequestBody @Valid InvokeInvitationRequest request
    ) {
        invitationService.invoke(request);
        return MessageResponse.ok("Invitation invoked");
    }

    @GetMapping("list")
    @Operation(summary = "List all invitations")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invitations listed successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Permission denied"),
    })
    @ValidatePermission(AuthLevel.ADMIN)
    @ValidatePagination
    public DataResponse<PageListDto<Invitation>> list(
            @RequestParam(value = "p", defaultValue = "1") @Page int page,
            @RequestParam(value = "ps", defaultValue = "1000") @PageSize int pageSize,
            @RequestParam(value = "type", defaultValue = "0") int type
    ) {
        ListInvitationRequest request = new ListInvitationRequest(page, pageSize, type);
        request.validate();
        var result = invitationService.list(request);
        return DataResponse.ok("Invitations listed", result);
    }
}
