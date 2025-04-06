package top.tony.spendit.api.modules.sync.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import top.tony.spendit.api.modules.sync.models.TagJson;

@Data
public class PushTagRequest {
    @NotNull
    private TagJson tags;
}
