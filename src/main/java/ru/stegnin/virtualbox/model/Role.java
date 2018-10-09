package ru.stegnin.virtualbox.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(callSuper = true, exclude = "users")
public class Role extends AbstractEntity implements Serializable {
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public static class Builder {
        private Role newRole;

        public Builder() {
            newRole = new Role();
        }

        public Builder withName(String name) {
            newRole.name = name;
            return this;
        }

        public Role build() {
            return newRole;
        }
    }
}
