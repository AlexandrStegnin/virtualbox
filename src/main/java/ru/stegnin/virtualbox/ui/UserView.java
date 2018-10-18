package ru.stegnin.virtualbox.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.stegnin.virtualbox.api.model.User;
import ru.stegnin.virtualbox.api.repository.UserRepository;
import ru.stegnin.virtualbox.settings.support.Constants;

@Route("users")
public class UserView extends VerticalLayout {
    private final UserRepository repo;
    private final Grid<User> grid;

    public UserView(UserRepository repo) {
        this.repo = repo;
        this.grid = new Grid<>(User.class);
        add(grid);
        listUsers();
        Button button = new Button("Выйти", event -> {
            SecurityContextHolder.clearContext();
            this.getUI().ifPresent(ui -> ui.navigate(Constants.LOGIN_URL.replace("/", "")));
        });
        add(button);
    }

    private void listUsers() {
        grid.setItems(repo.findAll());
    }
}
