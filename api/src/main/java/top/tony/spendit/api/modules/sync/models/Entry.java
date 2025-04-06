package top.tony.spendit.api.modules.sync.models;

import lombok.Data;

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
    private Integer money;
    private List<String> categories = new ArrayList<>();
    private List<String> people = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private String note;
}
