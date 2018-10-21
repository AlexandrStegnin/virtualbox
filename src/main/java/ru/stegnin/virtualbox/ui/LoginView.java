package ru.stegnin.virtualbox.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import ru.stegnin.virtualbox.api.repository.AuthRepository;

@Route("login")
public class LoginView extends VerticalLayout {

    private final AuthRepository authRepository;

    public LoginView(AuthRepository authRepository) {
        this.authRepository = authRepository;
        init();
    }

    private void init() {
        FormLayout loginForm = new FormLayout();

        TextField loginField = new TextField();
        loginField.setLabel("Имя пользователя");
        loginField.setPlaceholder("Логин");

        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Пароль");
        passwordField.setPlaceholder("*****");

        Button loginButton = new Button("Войти", e -> {
            if (authenticated(loginField.getValue(), passwordField.getValue()))
                this.getUI().ifPresent(ui -> ui.navigate("users"));
        });

        loginForm.add(loginField, passwordField, loginButton);
        add(loginForm);

        setMargin(true);
        setAlignItems(Alignment.CENTER);
    }

    private boolean authenticated(String login, String password) {
        return authRepository.authenticate(login, password).isAuthenticated();
    }

}
