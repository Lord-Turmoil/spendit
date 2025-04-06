/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.invitation.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tony.spendit.api.common.dto.PageListDto;
import top.tony.spendit.api.exceptions.ForbiddenException;
import top.tony.spendit.api.exceptions.NotFoundException;
import top.tony.spendit.api.models.Invitation;
import top.tony.spendit.api.models.repos.InvitationRepository;
import top.tony.spendit.api.modules.invitation.dto.CreateInvitationRequest;
import top.tony.spendit.api.modules.invitation.dto.InvokeInvitationRequest;
import top.tony.spendit.api.modules.invitation.dto.ListInvitationRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationService {
    private final InvitationRepository invitationRepository;

    public Invitation create(CreateInvitationRequest request) {
        Invitation invitation = new Invitation(request.isAdmin());
        invitationRepository.save(invitation);
        return invitation;
    }

    public void invoke(InvokeInvitationRequest request) {
        Optional<Invitation> result = invitationRepository.findById(request.getId());
        if (result.isEmpty()) {
            throw new NotFoundException("Invitation not found");
        }

        Invitation invitation = result.get();
        if (invitation.isAccepted()) {
            throw new ForbiddenException("Invitation accepted");
        }
        if (invitation.isInvoked()) {
            return;
        }

        invitation.setInvokedAt(LocalDateTime.now());
        invitationRepository.save(invitation);
    }

    /**
     * TODO: implement pagination
     */
    public PageListDto<Invitation> list(ListInvitationRequest request) {
        List<Invitation> all = invitationRepository.findAll();
        List<Invitation> invitations = switch (request.getType()) {
            case 1 -> all.stream().filter(Invitation::isAvailable).toList();
            case 2 -> all.stream().filter(Invitation::isInvoked).toList();
            case 3 -> all.stream().filter(Invitation::isAccepted).toList();
            default -> all;
        };
        return PageListDto.of(invitations, invitations.size(), request.getPage(), request.getPageSize());
    }
}
