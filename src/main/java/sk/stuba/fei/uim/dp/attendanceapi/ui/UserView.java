package sk.stuba.fei.uim.dp.attendanceapi.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Role;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.service.RoleService;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;
import sk.stuba.fei.uim.dp.attendanceapi.ui.component.AddRoleDialog;
import sk.stuba.fei.uim.dp.attendanceapi.ui.component.DeleteRoleDialog;
import sk.stuba.fei.uim.dp.attendanceapi.ui.component.UserForm;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "", layout = MainLayout.class)
@CssImport("./styles/shared-styles.css")
@AnonymousAllowed
public class UserView extends VerticalLayout {
    Grid<User> grid = new Grid<>(User.class);
    private final UserForm form;
    TextField nameFilterText = createFilterHeader();
    TextField idFilterText = createFilterHeader();
    TextField emailFilterText = createFilterHeader();
    TextField typeFilterText = createFilterHeader();
    TextField createdAtFilterText = createFilterHeader();
    TextField roleFilterText = createFilterHeader();

    private final AddRoleDialog addRoleDialog;
    private final DeleteRoleDialog deleteRoleDialog;
    private UserService userService;
    private RoleService roleService;
    private List<User> users;

    public UserView(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
        users = this.userService.getAll();
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new UserForm(this.roleService.getAll());
        form.addListener(UserForm.SaveEvent.class, this::saveUser);
        form.addListener(UserForm.DeleteEvent.class, this::deleteUser);
        form.addListener(UserForm.CloseEvent.class, e -> closeEditor());

        addRoleDialog = new AddRoleDialog();
        addRoleDialog.addListener(AddRoleDialog.AddEvent.class, this::addRole);
        deleteRoleDialog = new DeleteRoleDialog();
        deleteRoleDialog.addListener(DeleteRoleDialog.DeleteEvent.class, this::deleteRole);

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content, addRoleDialog, deleteRoleDialog);
        updateList(users);
        closeEditor();
    }

    private HorizontalLayout getToolbar(){
        Button addUser = new Button("Add user", click -> addUser());
        Button addRole = new Button("Add role", click -> addRoleDialog.open());
        Button deleteRole = new Button("Delete role", click -> deleteRoleDialog.open(this.roleService.getAll()));
        deleteRole.addThemeVariants(ButtonVariant.LUMO_ERROR);

        return new HorizontalLayout(addUser, addRole, deleteRole);
    }

    private void deleteRole(DeleteRoleDialog.DeleteEvent event) {
        this.roleService.delete(event.getRole());
        deleteRoleDialog.close();
    }

    private void addRole(AddRoleDialog.AddEvent event) {
        try{
            roleService.save(event.getRole());
            addRoleDialog.close();
            addRoleDialog.setRoleName("");
        }catch (Exception e){
            Notification.show("Failed to add role");
        }
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editUser(new User());
    }

    private void deleteUser(UserForm.DeleteEvent event) {
        userService.deleteUser(event.getUser().getId());
        updateList(null);
        closeEditor();
    }

    private void saveUser(UserForm.SaveEvent event) {
        try {
            userService.save(event.getUser());
            updateList(null);
            closeEditor();
        } catch (Exception e) {
            Notification.show("Failed to save user");
        }
    }

    private void closeEditor() {
        form.setUser(null, null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList(List<User> list) {
        if(list == null){
            users = userService.getAll();
            grid.setItems(users);
        }else{
            grid.setItems(list);
        }
    }

    private List<User> filterList(){
        return users.stream()
            .filter(user ->
                    user.getFullName().toUpperCase().contains(nameFilterText.getValue().toUpperCase())
                    && user.getEmail().toUpperCase().contains(emailFilterText.getValue().toUpperCase())
                    && user.getType().toString().toUpperCase().contains(typeFilterText.getValue().toUpperCase())
                    && user.getCreatedAt().toString().contains(createdAtFilterText.getValue())
                    && user.getId().toString().contains(idFilterText.getValue())

            )
            .collect(Collectors.toList());
    }

    private void configureGrid() {
        grid.addClassName("user-grid");
        setSizeFull();
        grid.removeColumnByKey("roles");
        grid.setColumns("id", "fullName", "email", "type", "createdAt");
        grid.addColumn(user -> {
            List<Role> roles = user.getRoles();
            return roles.toString().substring(1, roles.toString().length() - 1);
        }).setHeader("Role").setKey("roles").setSortable(true);

        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(grid.getColumnByKey("id")).setComponent(idFilterText);
        headerRow.getCell(grid.getColumnByKey("fullName")).setComponent(nameFilterText);
        headerRow.getCell(grid.getColumnByKey("email")).setComponent(emailFilterText);
        headerRow.getCell(grid.getColumnByKey("type")).setComponent(typeFilterText);
        headerRow.getCell(grid.getColumnByKey("createdAt")).setComponent(createdAtFilterText);
        headerRow.getCell(grid.getColumnByKey("roles")).setComponent(roleFilterText);

        grid.asSingleSelect().addValueChangeListener(e -> editUser(e.getValue()));
    }

    private void editUser(User user) {
        if(user == null){
            closeEditor();
        }else{
            form.setUser(user, userService.getAttendedActivities(user.getId()));
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private TextField createFilterHeader(){
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.LAZY);
        textField.getStyle().set("max-width", "100%");
        textField.setClearButtonVisible(true);
        textField.setWidthFull();
        textField.addValueChangeListener(e -> {
            updateList(filterList());
        });

        return textField;
    }
}
