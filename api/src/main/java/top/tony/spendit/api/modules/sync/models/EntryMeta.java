package top.tony.spendit.api.modules.sync.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.tony.spendit.api.common.Globals;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryMeta {
    /**
     * Day timestamp (YYYY-MM-DD)
     */
    private String timestamp;

    @JsonFormat(pattern = Globals.ISO_DATE_FORMAT)
    private LocalDateTime updated;
}
