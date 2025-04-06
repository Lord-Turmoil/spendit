/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.sync.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EntryMergeHint {
    private List<String> modify = new ArrayList<>();
    private List<String> send = new ArrayList<>();
    private List<String> receive = new ArrayList<>();
}
