package sk.stuba.fei.uim.dp.attendanceapi.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import sk.stuba.fei.uim.dp.attendanceapi.service.UserService;

import java.util.List;
import java.util.Map;

@Route("resetPassword")
@AnonymousAllowed
public class ResetPasswordView extends VerticalLayout
        implements HasUrlParameter<String>
{
    PasswordField password = new PasswordField("New password");
    PasswordField confirmPassword = new PasswordField("Confirm new password");
    Button save = new Button("Save");
    boolean isTokenValid;
    String token;

    UserService userService;
    public ResetPasswordView(UserService userService){
        this.userService = userService;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        H1 header = new H1("Reset password");
        configureButton();
        add(
                header,
                password,
                confirmPassword,
                save
        );
    }

    private void configureButton() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(click -> validateAndSave());
    }

    private void validateAndSave() {
        if(isTokenValid && isPasswordValid()){
            savePassword();
        }
    }

    private void savePassword() {
        try{
            userService.changePassword(
                    userService.getUserByPasswordResetToken(token),
                    password.getValue()
            );
            Notification.show("Successfully changed password");
        }catch (Exception e){
            Notification.show("Failed to change password");
        }
    }

    private boolean isPasswordValid() {
        boolean isValid = true;
        if(password.getValue().length() < 6){
            password.setErrorMessage("Must be 6 or more characters");
            password.setInvalid(true);
            isValid = false;
        }
        if(!password.getValue().equals(confirmPassword.getValue())){
            confirmPassword.setErrorMessage("Passwords do not match");
            confirmPassword.setInvalid(true);
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent,
                             @OptionalParameter String parameter) {
                Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();

        Map<String, List<String>> parametersMap = queryParameters
                .getParameters();
        if(parametersMap.containsKey("token") &&
                userService.validatePasswordResetToken(parametersMap.get("token").getFirst())) {
            isTokenValid = true;
            token = parametersMap.get("token").getFirst();
            return;
        }
        isTokenValid = false;
        Notification.show("Token is invalid");
    }
}
