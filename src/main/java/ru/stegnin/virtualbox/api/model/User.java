package ru.stegnin.virtualbox.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@ToString(exclude = "role")
@Table(name = "ApplicationUser")
@EqualsAndHashCode(callSuper = true, exclude = "role")
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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "UsersRoles",
            joinColumns = {@JoinColumn(name = "UserId")},
            inverseJoinColumns = {@JoinColumn(name = "RoleId")}
    )
    private Role role;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserSettings settings;

    @PrePersist
    public void setDefaultSettings() {
        setSettings(new UserSettings());
    }
}
