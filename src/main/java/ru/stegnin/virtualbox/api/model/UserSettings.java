package ru.stegnin.virtualbox.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class UserSettings extends AbstractEntity implements Serializable {

    @NotNull
    private String syncFolder = "../temp/";

}
