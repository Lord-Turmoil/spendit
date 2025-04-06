/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package top.tony.spendit.api.modules.bucket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bucket")
@Data
public class BucketOptions {
    private boolean serve;
    private String publicRoot;
    private String privateRoot;
}
