/*
 * Copyright (C) Tony's Studio 2025.
 */

package top.tony.spendit.api.modules.sync.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.tony.spendit.api.common.Globals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class TagJson {
    private List<TagCategory> categories = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private List<String> people = new ArrayList<>();

    @JsonFormat(pattern = Globals.ISO_DATE_FORMAT)
    private LocalDateTime updated;

    public void merge(TagJson other) {
        for (TagCategory category : other.getCategories()) {
            if (this.categories.stream().noneMatch(c -> c.getPrimary().equals(category.getPrimary()))) {
                this.categories.add(category);
            } else {
                this.categories.forEach(existingCategory -> {
                    if (existingCategory.getPrimary().equals(category.getPrimary())) {
                        for (String secondary : category.getSecondaries()) {
                            if (!existingCategory.getSecondaries().contains(secondary)) {
                                existingCategory.getSecondaries().add(secondary);
                            }
                        }
                    }
                });
            }
        }
        for (TagCategory category : this.categories) {
            category.getSecondaries().sort(String::compareTo);
        }
        categories.sort(Comparator.comparing(TagCategory::getPrimary));

        for (String tag : other.getTags()) {
            if (!this.tags.contains(tag)) {
                this.tags.add(tag);
            }
        }
        tags.sort(String::compareTo);

        for (String person : other.getPeople()) {
            if (!this.people.contains(person)) {
                this.people.add(person);
            }
        }
        people.sort(String::compareTo);
    }
}
