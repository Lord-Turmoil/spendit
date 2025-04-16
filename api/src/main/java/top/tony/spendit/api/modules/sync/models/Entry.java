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

/**
 * This class simply provides types for now.
 */
@Data
public class Entry {
    private String title;
    private String date;
    private Long timestamp;
    @JsonFormat(pattern = Globals.ISO_DATE_FORMAT)
    private LocalDateTime updated;
    private Integer money;
    private List<String> categories = new ArrayList<>();
    private List<String> people = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private String note;
}
