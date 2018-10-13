package ru.stegnin.virtualbox.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@ToString(exclude = "roles")
@Table(name = "ApplicationUser")
@EqualsAndHashCode(callSuper = true, exclude = "roles")
public class User extends AbstractEntity implements Serializable {
    @NotNull
    @Column(unique = true, nullable = false)
    private String login = "";

    @NotNull
    @Column(unique = true, nullable = false)
    private String email = "";

    @NotNull
    @Column(nullable = false)
    private String password = "";

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "UsersRoles",
            joinColumns = { @JoinColumn(name = "UserId") },
            inverseJoinColumns = { @JoinColumn(name = "RoleId") }
    )
    private Set<Role> roles;

    public static class Builder {
        private User newUser;

        public Builder() {
            newUser = new User();
        }

        public Builder withLogin(String login) {
            newUser.login = login;
            return this;
        }

        public Builder withEmail(String email) {
            newUser.email = email;
            return this;
        }

        public Builder withPassword(String password) {
            newUser.password = password;
            return this;
        }

        public User build() {
            return newUser;
        }
    }
}
