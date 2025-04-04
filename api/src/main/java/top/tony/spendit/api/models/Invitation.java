package top.tony.spendit.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import top.tony.spendit.api.common.Globals;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;
    private boolean admin;

    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime createdAt;
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime invokedAt;
    @JsonFormat(pattern = Globals.DATE_FORMAT)
    private LocalDateTime acceptedAt;

    public Invitation(boolean admin) {
        this.code = RandomStringUtils.secure().nextAlphanumeric(8);
        this.admin = admin;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isInvoked() {
        return invokedAt != null;
    }

    public boolean isAccepted() {
        return acceptedAt != null;
    }

    public boolean isAvailable() {
        return !isInvoked() && !isAccepted();
    }
}
