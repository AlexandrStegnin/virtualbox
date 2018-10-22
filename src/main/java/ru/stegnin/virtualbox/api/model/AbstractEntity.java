package ru.stegnin.virtualbox.api.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import ru.stegnin.virtualbox.settings.support.CustomDateSerializer;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @NotNull
    private String id = "";

    @NotNull
    @JsonSerialize(using = CustomDateSerializer.class)
    private LocalDate created;

    @NotNull
    @JsonSerialize(using = CustomDateSerializer.class)
    private LocalDate updated;

    @PrePersist
    private void prePersist() {
        id = UUID.randomUUID().toString().replace("-", "");
        created = LocalDate.now();
        updated = LocalDate.now();
    }

    @PreUpdate
    private void preUpdate() {
        if (created == null) created = LocalDate.now();
        updated = LocalDate.now();
    }
}
