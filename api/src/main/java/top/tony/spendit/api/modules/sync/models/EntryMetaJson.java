/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.sync.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EntryMetaJson {
    private List<EntryMeta> entries = new ArrayList<>();

    public void update(EntryMeta entry) {
        if (entries.stream().noneMatch(e -> e.getTimestamp().equals(entry.getTimestamp()))) {
            entries.add(entry);
            return;
        }

        entries.forEach(e -> {
            if (e.getTimestamp().equals(entry.getTimestamp())) {
                e.setUpdated(entry.getUpdated());
            }
        });
    }
}
