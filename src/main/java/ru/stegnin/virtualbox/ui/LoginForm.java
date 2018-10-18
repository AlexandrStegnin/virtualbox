package ru.stegnin.virtualbox.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stegnin.virtualbox.api.repository.AuthRepository;

@Route("login")
public class LoginForm extends VerticalLayout {

    @Autowired
    private AuthRepository authRepository;

    public LoginForm() {
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
            if (authRepository.authenticate(loginField.getValue(), passwordField.getValue()).isAuthenticated())
                this.getUI().ifPresent(ui -> ui.navigate("users"));
        });

        loginForm.add(loginField, passwordField, loginButton);
        add(loginForm);
    }

}
