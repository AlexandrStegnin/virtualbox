package ru.stegnin.virtualbox.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@ToString(exclude = "users")
@JsonIgnoreProperties("users")
@EqualsAndHashCode(callSuper = true, exclude = "users")
public class Role extends AbstractEntity implements Serializable, GrantedAuthority {
    @JsonIgnore
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

    @Override
    public String getAuthority() {
        return name;
    }
}
