package ru.stegnin.virtualbox.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.Route;
import ru.stegnin.virtualbox.api.model.Role;
import ru.stegnin.virtualbox.api.model.User;
import ru.stegnin.virtualbox.api.model.User_;
import ru.stegnin.virtualbox.api.repository.AuthRepository;
import ru.stegnin.virtualbox.api.repository.RoleRepository;
import ru.stegnin.virtualbox.api.repository.UserRepository;
import ru.stegnin.virtualbox.settings.support.Constants;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Route("users")
public class UserView extends HorizontalLayout {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final AuthRepository auth;
    private final Grid<User> grid;
    private List<User> inMemoryUsers = new ArrayList<>();
    private ListDataProvider<User> dataProvider;

    public UserView(UserRepository userRepo, AuthRepository auth, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.auth = auth;
        this.roleRepo = roleRepo;
        this.grid = new Grid<>(User.class);
        inMemoryUsers.addAll(userRepo.findAll());
        dataProvider = DataProvider.ofCollection(inMemoryUsers);
        init();
    }

    private void init() {
        // TODO: 21.10.2018 Добавить привязку ролей к пользователю в/из roleComboBox
        dataProvider.setSortOrder(User::getLogin, SortDirection.DESCENDING);
        grid.setDataProvider(dataProvider);
        getUsers();

        grid.setColumns(User_.ID, User_.LOGIN, User_.EMAIL);
        grid.addColumn(new LocalDateRenderer<>(User::getCreated,
                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        ).setHeader("Создан");
        grid.addColumn(new LocalDateRenderer<>(User::getUpdated,
                DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        ).setHeader("Изменён");
        grid.addColumn(new ComponentRenderer<>(user -> {
            Icon check;
            if (user.isEnabled()) {
                check = new Icon(VaadinIcon.CHECK);
                check.setColor("green");
                return check;
            } else {
                check = new Icon(VaadinIcon.CLOSE_SMALL);
                check.setColor("red");
                return check;
            }
        })).setHeader("Активный");

        grid.addColumn(user -> {
            List<String> roles = new ArrayList<>();
            user.getRoles().forEach(r -> roles.add(r.getName()));
            return String.join(", ", roles);
        }).setHeader("Роли");

        grid.getColumnByKey(User_.ID).setVisible(false);
        grid.getColumnByKey(User_.LOGIN).setHeader("Логин");
        grid.getColumnByKey(User_.EMAIL).setHeader("Email");

        grid.addComponentColumn(user -> new NativeButton("Удалить", evt -> {
            dataProvider.getItems().remove(user);
            dataProvider.refreshAll();
            userRepo.delete(user);
        })).setHeader("Действия");

        FormLayout layoutWithBinder = new FormLayout();
        Binder<User> binder = new Binder<>();

        // The object that will be edited
        User userBeingEdited = new User();

        // Create the fields
        TextField login = new TextField();
        login.setValueChangeMode(ValueChangeMode.EAGER);

        TextField email = new TextField();
        email.setValueChangeMode(ValueChangeMode.EAGER);

        PasswordField password = new PasswordField();
        password.setValueChangeMode(ValueChangeMode.EAGER);

        ComboBox<Role> roleComboBox = new ComboBox<>();
        roleComboBox.setItemLabelGenerator(Role::getName);
        roleComboBox.setItems(roleRepo.findAll());

        Checkbox enabled = new Checkbox();
        Label infoLabel = new Label();
        NativeButton save = new NativeButton("Сохранить");
        NativeButton reset = new NativeButton("Сбросить");

        layoutWithBinder.addFormItem(login, "Логин");
        layoutWithBinder.addFormItem(email, "E-mail");
        layoutWithBinder.addFormItem(password, "Пароль");
        layoutWithBinder.addFormItem(roleComboBox, "Роли");
        layoutWithBinder.addFormItem(enabled, "Активный");

        // Button bar
        HorizontalLayout actions = new HorizontalLayout();
        actions.add(save, reset);
        actions.setSpacing(true);
        actions.setMargin(true);

        // E-mail have specific validator
        SerializablePredicate<String> emailPredicate = value -> !email.getValue().trim().isEmpty();
        Binder.Binding<User, String> emailBinding = binder.forField(email)
                .withValidator(emailPredicate,
                        "Email cannot be empty")
                .withValidator(new EmailValidator("Incorrect email address"))
                .bind(User::getEmail, User::setEmail);

        // Trigger cross-field validation when the other field is changed
        email.addValueChangeListener(event -> emailBinding.validate());

        // Login and email are required fields
        login.setRequiredIndicatorVisible(true);
        email.setRequiredIndicatorVisible(true);

        binder.forField(login)
                .withValidator(new StringLengthValidator(
                        "Please add the login", 3, null))
                .bind(User::getLogin, User::setLogin);
        binder.forField(email)
                .withValidator(new EmailValidator("This is not email"))
                .bind(User::getEmail, User::setEmail);

        binder.forField(password).bind(User::getPassword, User::setPassword);
        // Enabled don't need any special validators
        binder.bind(enabled, User::isEnabled, User::setEnabled);

        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(userBeingEdited)) {
                infoLabel.setText("Saved bean values: " + userBeingEdited);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(() -> {
                    inMemoryUsers.add(userBeingEdited);
                    dataProvider.getItems().add(userBeingEdited);
                    dataProvider.refreshAll();
                    syncUsers();
                });
            } else {
                BinderValidationStatus<User> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                infoLabel.setText("There are errors: " + errorText);
            }
        });
        reset.addClickListener(event -> {
            // clear fields by setting null
            binder.readBean(null);
            infoLabel.setText("");
            enabled.setValue(false);
        });

        grid.asSingleSelect();
        grid.addSelectionListener(e -> {
            Optional<User> user = e.getFirstSelectedItem();
            user.ifPresent(user1 -> user1.setPassword(""));
            user.ifPresent(binder::readBean);
        });

        layoutWithBinder.add(infoLabel, actions);

        Button logout = new Button("Выйти", event -> logout());
        HorizontalLayout header = new HorizontalLayout();
        header.setHeight("100%");
        header.add(logout);
        VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setWidth("300px");
        rightLayout.add(layoutWithBinder);
        add(grid, rightLayout, header);

    }

    private void getUsers() {
        dataProvider = DataProvider.ofCollection(inMemoryUsers);
    }

    protected void syncUsers() {
        inMemoryUsers = userRepo.saveAll(inMemoryUsers);
    }

    private void logout() {
        this.getUI().ifPresent(ui -> ui.navigate(Constants.LOGIN_URL.replace("/", "")));
        auth.logout();
    }
}
