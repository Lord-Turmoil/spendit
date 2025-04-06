/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.account.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tony.spendit.api.aspect.ValidateParameters;
import top.tony.spendit.api.common.dto.DataResponse;
import top.tony.spendit.api.common.dto.MessageResponse;
import top.tony.spendit.api.common.requets.BaseController;
import top.tony.spendit.api.exceptions.InternalServerErrorException;
import top.tony.spendit.api.extensions.jwt.JwtIssueException;
import top.tony.spendit.api.models.Account;
import top.tony.spendit.api.modules.account.dto.AccountDto;
import top.tony.spendit.api.modules.account.dto.LoginRequest;
import top.tony.spendit.api.modules.account.dto.RegisterRequest;
import top.tony.spendit.api.modules.account.services.AccountService;
import top.tony.spendit.api.modules.auth.api.AuthApi;
import top.tony.spendit.api.modules.auth.aspect.ValidatePermission;
import top.tony.spendit.api.modules.auth.models.AuthPayload;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication")
public class AuthController extends BaseController {
    private final AccountService accountService;
    private final AuthApi authApi;

    @PostMapping("register")
    @Operation(summary = "Register an account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Bad request, duplicated username"),
            @ApiResponse(responseCode = "401", description = "Not invited"),
    })
    @ValidateParameters
    public DataResponse<AccountDto> register(
            @RequestBody @Valid RegisterRequest request
    ) {
        Account account = accountService.register(request);
        return DataResponse.ok("Welcome to SpendIt!", mappers.map(account, AccountDto.class));
    }

    @PostMapping("login")
    @Operation(summary = "Login an account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
    })
    public DataResponse<AccountDto> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response
    ) {
        Account account = accountService.login(request);
        issueJwt(response, account);
        return DataResponse.ok("Login successful", mappers.map(account, AccountDto.class));
    }

    @PostMapping("logout")
    @Operation(summary = "Logout an account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "401", description = "Not logged in"),
    })
    @ValidatePermission
    public MessageResponse logout(HttpServletResponse response) {
        response.addCookie(authApi.cleanJwtCookie());
        response.addCookie(authApi.cleanRefreshCookie());
        return MessageResponse.ok("Logout successful");
    }

    private void issueJwt(HttpServletResponse response, Account account) {
        try {
            AuthPayload auth = mappers.map(account, AuthPayload.class);
            String jwt = authApi.issueJwt(auth);
            String refresh = authApi.issueRefresh(auth);
            response.addCookie(authApi.setJwtCookie(jwt));
            response.addCookie(authApi.setRefreshCookie(refresh));
        } catch (JwtIssueException e) {
            throw new InternalServerErrorException("Failed to issue JWT token");
        }
    }
}
