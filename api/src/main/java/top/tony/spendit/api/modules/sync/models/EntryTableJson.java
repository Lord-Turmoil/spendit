/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.sync.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.tony.spendit.api.common.Globals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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

        int index = 0;
        for (Entry otherEntry : other.entries) {
            if (otherEntry.getUpdated() == null) {
                otherEntry.setUpdated(LocalDateTime.now());
            }

            boolean isNew = true;
            for (Entry entry : entries) {
                if (entry.getUpdated() == null) {
                    entry.setUpdated(LocalDateTime.now());
                }

                if (entry.getTimestamp().equals(otherEntry.getTimestamp())) {
                    if (otherEntry.getUpdated().isAfter(entry.getUpdated())) {
                        entries.set(index, otherEntry);
                    }
                    isNew = false;
                }
            }

            if (isNew) {
                entries.add(otherEntry);
            }

            index++;
        }

        entries.sort(Comparator.comparing(Entry::getTimestamp));
    }
}
