/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.invitation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.tony.spendit.api.aspect.IRequireValidation;
import top.tony.spendit.api.exceptions.BadRequestException;

@Data
@AllArgsConstructor
public class ListInvitationRequest implements IRequireValidation {
    private int page;
    private int pageSize;
    /**
     * 0: all
     * 1: available
     * 2: invoked
     * 3: accepted
     */
    private int type;

    @Override
    public void validate() {
        if (type < 0 || type > 3) {
            throw new BadRequestException("Invalid invitation type");
        }
    }

    public int getOffset() {
        return (page - 1) * pageSize;
    }

    public int getLimit() {
        return pageSize;
    }
}
