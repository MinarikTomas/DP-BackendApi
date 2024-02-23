package sk.stuba.fei.uim.dp.attendanceapi.ui;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        var login = new LoginForm();

        add(
                login
        );
    }
}
