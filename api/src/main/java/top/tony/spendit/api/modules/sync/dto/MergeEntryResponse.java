package top.tony.spendit.api.modules.sync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.tony.spendit.api.modules.sync.models.EntryMergeHint;
import top.tony.spendit.api.modules.sync.models.EntryTableJson;

import java.util.List;

@Data
@AllArgsConstructor
public class MergeEntryResponse {
    EntryMergeHint hint;
    List<EntryTableJson> entries;
}
