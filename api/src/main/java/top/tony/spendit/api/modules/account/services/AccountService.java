package top.tony.spendit.api.modules.account.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tony.spendit.api.common.requets.BaseService;
import top.tony.spendit.api.exceptions.BadRequestException;
import top.tony.spendit.api.exceptions.UnauthorizedException;
import top.tony.spendit.api.models.Account;
import top.tony.spendit.api.models.AccountInvitation;
import top.tony.spendit.api.models.Invitation;
import top.tony.spendit.api.models.repos.AccountInvitationRepository;
import top.tony.spendit.api.models.repos.AccountRepository;
import top.tony.spendit.api.models.repos.InvitationRepository;
import top.tony.spendit.api.modules.account.dto.LoginRequest;
import top.tony.spendit.api.modules.account.dto.RegisterRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService extends BaseService {
    private final InvitationRepository invitationRepository;
    private final AccountRepository accountRepository;
    private final AccountInvitationRepository accountInvitationRepository;

    public Account register(RegisterRequest request) {
        Invitation invitation = validateInvitation(request);
        validateUsername(request);
        return createNewAccount(request, invitation);
    }

    public Account login(LoginRequest request) {
        Account account = accountRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        if (account == null) {
            throw new UnauthorizedException("用户名或密码错误");
        }
        return account;
    }

    private Invitation validateInvitation(RegisterRequest request) {
        // find invitation
        Invitation invitation = invitationRepository.findByCodeEquals(request.getCode());
        if (invitation == null) {
            throw new UnauthorizedException("无效的邀请码");
        }
        if (invitation.isInvoked()) {
            throw new UnauthorizedException("邀请码已失效");
        }
        if (invitation.isAccepted()) {
            throw new UnauthorizedException("邀请码已被使用");
        }
        return invitation;
    }

    private void validateUsername(RegisterRequest request) {
        Account old = accountRepository.findByUsername(request.getUsername());
        if (old != null) {
            throw new BadRequestException("用户名已存在");
        }
    }

    private Account createNewAccount(RegisterRequest request, Invitation invitation) {
        Account account = new Account(request.getUsername(), request.getPassword(), invitation.isAdmin());

        accountRepository.save(account);
        invitation.setAcceptedAt(account.getCreatedAt());
        invitationRepository.save(invitation);
        accountInvitationRepository.save(new AccountInvitation(account.getId(), invitation.getId()));

        return account;
    }
}
