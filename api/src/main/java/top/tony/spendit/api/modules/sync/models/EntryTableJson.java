/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.sync.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.tony.spendit.api.common.Globals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class EntryTableJson {
    /**
     * Day timestamp (YYYY-MM-DD)
     */
    private String timestamp;

    @JsonFormat(pattern = Globals.ISO_DATE_FORMAT)
    private LocalDateTime updated;

    private List<Entry> entries = new ArrayList<>();

    public void merge(EntryTableJson other) {
        if (!other.timestamp.equals(timestamp)) {
            return;
        }
        for (Entry entry : other.entries) {
            if (entries.stream().noneMatch(e -> e.getTimestamp().equals(entry.getTimestamp()))) {
                entries.add(entry);
            }
        }
    }
}
