package ru.stegnin.virtualbox.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@ToString(exclude = "users")
@JsonIgnoreProperties("users")
@EqualsAndHashCode(callSuper = true, exclude = "users")
public class Role extends AbstractEntity implements Serializable {
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<User> users;
}
