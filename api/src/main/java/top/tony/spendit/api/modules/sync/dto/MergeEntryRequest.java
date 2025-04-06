package top.tony.spendit.api.modules.sync.dto;

import lombok.Data;
import top.tony.spendit.api.modules.sync.models.EntryMergeHint;
import top.tony.spendit.api.modules.sync.models.EntryTableJson;

import java.util.List;

@Data
public class MergeEntryRequest {
    private EntryMergeHint hint;
    private List<EntryTableJson> entries;
}
