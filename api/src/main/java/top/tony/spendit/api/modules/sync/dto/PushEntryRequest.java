/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.sync.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import top.tony.spendit.api.modules.sync.models.EntryTableJson;

import java.util.List;

@Data
public class PushEntryRequest {
    @NotNull
    private List<EntryTableJson> entries;
}
