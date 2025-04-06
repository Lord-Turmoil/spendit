package top.tony.spendit.api.modules.sync.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TagCategory {
    private String primary;
    private List<String> secondaries = new ArrayList<>();
}