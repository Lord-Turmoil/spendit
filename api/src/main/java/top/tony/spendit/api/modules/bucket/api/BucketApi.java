/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.bucket.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.tony.spendit.api.common.Globals;
import top.tony.spendit.api.modules.bucket.services.PathService;

@Component
@RequiredArgsConstructor
public class BucketApi {
    private final PathService pathService;

    /**
     * @see PathService#recordToPublicPath(String)
     */
    public String recordToPublicPath(String record) {
        return pathService.recordToPublicPath(record);
    }

    /**
     * @see PathService#recordToPrivatePath(String)
     */
    public String recordToPrivatePath(String record) {
        return pathService.recordToPrivatePath(record);
    }

    /**
     * Get a random directory under temp directory.
     */
    public String getRandomTempPath() {
        return recordToPrivatePath(toRandomRecord(Globals.TEMP_TAG, ""));
    }

    /**
     * Get a temp directory with the given filename under temp directory.
     */
    public String getTempPath(String filename) {
        return recordToPrivatePath(toRecord(Globals.TEMP_TAG, filename));
    }

    /**
     * Get a random temp file with the given extension.
     */
    public String getRandomTempFile(String extension) {
        return getRandomTempPath() + "." + extension;
    }

    /**
     * Get a temp file with the given filename.
     */
    public String getTempFile(String filename) {
        return recordToPrivatePath(toRecord(Globals.TEMP_TAG, filename));
    }


    /**
     * @see PathService#toRecord(String, String)
     */
    public String toRandomRecord(String tag, String filename) {
        return pathService.toRecord(tag, filename);
    }

    /**
     * @see PathService#toRecord(String)
     */
    public String toRandomRecord(String filename) {
        return pathService.toRecord(filename);
    }

    /**
     * @see PathService#toRecord(String, String, boolean)
     */
    public String toRecord(String tag, String filename) {
        return pathService.toRecord(tag, filename, false);
    }

    public String toRecord(String tag, String... args) {
        return pathService.toRecord(tag, String.join("/", args), false);
    }

    /**
     * @see PathService#toRecord(String)
     */
    public String toRecord(String filename) {
        return pathService.toRecord(filename, false);
    }
}
