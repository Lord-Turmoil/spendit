/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.sync.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import top.tony.spendit.api.modules.sync.models.EntryMetaJson;

@Data
public class MergeEntryHintRequest {
    @NotNull
    private EntryMetaJson meta;
}
