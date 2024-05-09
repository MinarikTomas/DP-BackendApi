package sk.stuba.fei.uim.dp.attendanceapi.ui.component;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Role;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserForm extends FormLayout {

    TextField id = new TextField("Id");
    TextField fullName = new TextField("Full name");
    EmailField email = new EmailField("Email");

    PasswordField password = new PasswordField("Password");
    ComboBox<User.Type> type = new ComboBox<>("Type");

    MultiSelectComboBox<Role> roles = new MultiSelectComboBox<>("Role");
    VerticalLayout gridLayout = new VerticalLayout();
    Grid<Activity> attendedActivitesGrid = new Grid<>(Activity.class);

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    HorizontalLayout toolbar = new HorizontalLayout();

    Binder<User> binder = new BeanValidationBinder<>(User.class);

    public UserForm(List<Role> rolesList){
        addClassName("form");
        initBinder();

        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );

        id.setReadOnly(true);
        type.setItems(User.Type.values());
        roles.setItems(rolesList);
        roles.setItemLabelGenerator(Role::getName);
        gridLayout.add(
                new NativeLabel("Attended activities"),
                attendedActivitesGrid
        );

        attendedActivitesGrid.setColumns("id", "name", "time");

        binder.bindInstanceFields(this);
        createButtonsLayout();

        add(
                toolbar,
                id,
                fullName,
                email,
                password,
                type,
                roles,
                gridLayout
        );

        setColspan(toolbar, 2);
        setColspan(gridLayout, 2);
    }

    private void initBinder() {
        Converter<Set<Role>, List<Role>> setToListConverter = new Converter<Set<Role>, List<Role>>() {
            @Override
            public Result<List<Role>> convertToModel(Set<Role> roles, ValueContext valueContext) {
                List<Role> list = new ArrayList<>(roles);
                return Result.ok(list);
            }

            @Override
            public Set<Role> convertToPresentation(List<Role> roles, ValueContext valueContext) {
                return new HashSet<>(roles);
            }
        };

        binder.forField(roles)
                .withValidator(s -> !s.isEmpty(), "Please select at least one role")
                .withConverter(setToListConverter)
                .bind(User::getRoles, User::setRoles);
    }

    public void setUser(User user, List<Activity> attendedActivitiesList){
        if(user != null){
            if(user.getId() == null){
                delete.setVisible(false);
                id.setVisible(false);
                password.setVisible(true);
                gridLayout.setVisible(false);
            }else{
                id.setVisible(true);
                delete.setVisible(true);
                password.setVisible(false);
                attendedActivitesGrid.setItems(attendedActivitiesList);
                gridLayout.setVisible(true);
            }
        }
        binder.setBean(user);
    }

    private void createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(click -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        toolbar.add(save, delete, close);
    }

    private void validateAndSave() {
        if(binder.isValid()){
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    @Getter
    public static abstract class UserFormEvent extends ComponentEvent<UserForm>{
        private final User user;

        protected UserFormEvent(UserForm source, User user){
            super(source, false);
            this.user = user;
        }

    }

    public static class SaveEvent extends UserFormEvent{
        SaveEvent(UserForm source, User user){
            super(source, user);
        }
    }

    public static class DeleteEvent extends UserFormEvent{
        DeleteEvent(UserForm source, User user){
            super(source, user);
        }
    }

    public static class CloseEvent extends UserFormEvent{
        CloseEvent(UserForm source){
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener){
        return getEventBus().addListener(eventType, listener);
    }
}
