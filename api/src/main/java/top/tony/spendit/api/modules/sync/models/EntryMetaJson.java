package top.tony.spendit.api.modules.sync.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EntryMetaJson {
    private List<EntryMeta> entries = new ArrayList<>();

    public void update(EntryMeta entry) {
        entries.forEach(e -> {
            if (e.getTimestamp().equals(entry.getTimestamp())) {
                e.setUpdated(entry.getUpdated());
                return;
            }
        });
        entries.add(entry);
    }
}
