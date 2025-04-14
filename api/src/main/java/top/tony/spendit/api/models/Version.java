/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.tony.spendit.api.common.Globals;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class Version {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String version;

    private String downloadUrl;

    private String code;

    private String description;

    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime timestamp;

    public Version(String version, String downloadUrl, String code, String description) {
        this.version = version;
        this.downloadUrl = downloadUrl;
        this.code = code;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
}
