package top.tony.spendit.api.modules.sync.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tony.spendit.api.common.requets.BaseService;
import top.tony.spendit.api.common.utils.Medias;
import top.tony.spendit.api.exceptions.InternalServerErrorException;
import top.tony.spendit.api.modules.bucket.api.BucketApi;
import top.tony.spendit.api.modules.sync.dto.MergeEntryRequest;
import top.tony.spendit.api.modules.sync.models.EntryMergeHint;
import top.tony.spendit.api.modules.sync.models.EntryMeta;
import top.tony.spendit.api.modules.sync.models.EntryMetaJson;
import top.tony.spendit.api.modules.sync.models.EntryTableJson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class EntrySyncService extends BaseService {
    private final BucketApi bucketApi;

    public void push(String id, List<EntryTableJson> entries) {
        EntryMetaJson meta = new EntryMetaJson();
        for (EntryTableJson entry : entries) {
            meta.getEntries().add(new EntryMeta(
                    entry.getTimestamp(),
                    entry.getUpdated()
            ));
        }
        meta.getEntries().sort(Comparator.comparing(EntryMeta::getTimestamp));

        try {
            for (EntryTableJson entry : entries) {
                saveEntry(id, entry);
            }
            saveMeta(id, meta);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException("JSON 解析失败");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException("文件保存失败");
        }
    }

    public List<EntryTableJson> pull(String id) {
        List<EntryTableJson> entries = new ArrayList<>();
        try {
            EntryMetaJson meta = loadMeta(id);
            for (EntryMeta entry : meta.getEntries()) {
                entries.add(loadEntry(id, entry.getTimestamp()));
            }
            return entries;
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException("JSON 解析失败");
        } catch (IOException e) {
            throw new InternalServerErrorException("文件读取失败");
        }
    }

    public EntryMergeHint getMergeHint(String id, EntryMetaJson meta) {
        EntryMergeHint mergeHint = new EntryMergeHint();
        try {
            EntryMetaJson cloudMeta = loadMeta(id);
            for (EntryMeta entry : meta.getEntries()) {
                if (cloudMeta.getEntries().stream().noneMatch(e ->
                        e.getTimestamp().equals(entry.getTimestamp()))) {
                    mergeHint.getSend().add(entry.getTimestamp());
                } else if (cloudMeta.getEntries().stream().anyMatch(e ->
                        e.getTimestamp().equals(entry.getTimestamp()) && !e.getUpdated().equals(entry.getUpdated()))) {
                    mergeHint.getModify().add(entry.getTimestamp());
                }
            }
            for (EntryMeta entry : cloudMeta.getEntries()) {
                if (meta.getEntries().stream().noneMatch(e ->
                        e.getTimestamp().equals(entry.getTimestamp()))) {
                    mergeHint.getReceive().add(entry.getTimestamp());
                }
            }
            return mergeHint;
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException("JSON 解析失败");
        } catch (IOException e) {
            throw new InternalServerErrorException("文件读取失败");
        }
    }

    public List<EntryTableJson> merge(String id, MergeEntryRequest request) {
        EntryMergeHint hint = request.getHint();
        List<EntryTableJson> entries = request.getEntries();
        Map<String, EntryTableJson> map = new HashMap<>();

        for (EntryTableJson entry : entries) {
            map.put(entry.getTimestamp(), entry);
        }

        try {
            return mergeImpl(id, hint, map);
        } catch (IOException e) {
            throw new InternalServerErrorException("文件 I/O 失败");
        }
    }

    private List<EntryTableJson> mergeImpl(String id, EntryMergeHint hint, Map<String, EntryTableJson> entries) throws IOException {
        EntryMetaJson meta = loadMeta(id);
        List<EntryTableJson> merged = new ArrayList<>();

        // add entries to receive
        for (String timestamp : hint.getReceive()) {
            merged.add(loadEntry(id, timestamp));
        }

        // handle entries to add
        for (String timestamp : hint.getSend()) {
            EntryTableJson entry = entries.get(timestamp);
            if (entry != null) {
                meta.update(new EntryMeta(
                        entry.getTimestamp(),
                        entry.getUpdated()
                ));
                saveEntry(id, entry);
            }
        }

        // handle entries to merge
        for (String timestamp : hint.getModify()) {
            EntryTableJson entry = entries.get(timestamp);
            if (entry != null) {
                EntryTableJson mergedEntry = loadEntry(id, timestamp);
                mergedEntry.merge(entry);
                mergedEntry.setUpdated(LocalDateTime.now());
                meta.update(new EntryMeta(
                        mergedEntry.getTimestamp(),
                        mergedEntry.getUpdated()
                ));
                saveEntry(id, mergedEntry);
                merged.add(mergedEntry);
            }
        }

        saveMeta(id, meta);

        return merged;
    }

    private String getMetaFilePath(String id) {
        return bucketApi.recordToPrivatePath(bucketApi.toRecord(id, "meta.json"));
    }

    private String getEntryFilePath(String id, String timestamp) {
        return bucketApi.recordToPrivatePath(bucketApi.toRecord(id, "data", timestamp + ".json"));
    }

    private EntryMetaJson loadMeta(String id) throws IOException {
        String path = getMetaFilePath(id);
        if (!Medias.exists(path)) {
            return new EntryMetaJson();
        }
        String json = Medias.read(path);
        return mappers.fromJson(json, EntryMetaJson.class);
    }

    private void saveMeta(String id, EntryMetaJson meta) throws IOException {
        Medias.save(getMetaFilePath(id), mappers.toJson(meta));
    }

    private EntryTableJson loadEntry(String id, String timestamp) throws IOException {
        String json = Medias.read(getEntryFilePath(id, timestamp));
        return mappers.fromJson(json, EntryTableJson.class);
    }

    private void saveEntry(String id, EntryTableJson entry) throws IOException {
        Medias.save(getEntryFilePath(id, entry.getTimestamp()), mappers.toJson(entry));
    }
}
