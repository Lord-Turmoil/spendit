/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.sync.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tony.spendit.api.common.requets.BaseService;
import top.tony.spendit.api.common.utils.Medias;
import top.tony.spendit.api.exceptions.BadRequestException;
import top.tony.spendit.api.exceptions.InternalServerErrorException;
import top.tony.spendit.api.modules.bucket.api.BucketApi;
import top.tony.spendit.api.modules.sync.models.TagJson;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagSyncService extends BaseService {
    private final BucketApi bucketApi;

    public void push(String id, TagJson tags) {
        try {
            String json = mappers.toJson(tags);
            Medias.save(getTagFilePath(id), json);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException("JSON 解析失败");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new InternalServerErrorException("文件保存失败");
        }
    }

    public TagJson pull(String id) {
        try {
            String json = Medias.read(getTagFilePath(id));
            return mappers.fromJson(json, TagJson.class);
        } catch (JsonParseException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException("JSON 解析失败");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new InternalServerErrorException("文件读取失败");
        }
    }

    public TagJson merge(String id, TagJson tags) {
        try {
            String json = Medias.read(getTagFilePath(id));
            TagJson existingTags = mappers.fromJson(json, TagJson.class);
            existingTags.merge(tags);
            existingTags.setUpdated(LocalDateTime.now());
            String newJson = mappers.toJson(existingTags);
            Medias.save(getTagFilePath(id), newJson);
            return existingTags;
        } catch (JsonParseException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException("JSON 解析失败");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new InternalServerErrorException("文件 I/O 失败");
        }
    }

    private String getTagFilePath(String id) {
        return bucketApi.recordToPrivatePath(bucketApi.toRecord(id, "tags.json"));
    }
}
