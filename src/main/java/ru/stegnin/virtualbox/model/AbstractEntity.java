package ru.stegnin.virtualbox.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.stegnin.virtualbox.support.CustomDateSerializer;

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

    @Nullable
    @JsonSerialize(using = CustomDateSerializer.class)
    private LocalDate created;

    @Nullable
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
