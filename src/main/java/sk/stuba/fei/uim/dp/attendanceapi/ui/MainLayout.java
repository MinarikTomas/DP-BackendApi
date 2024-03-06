package sk.stuba.fei.uim.dp.attendanceapi.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import sk.stuba.fei.uim.dp.attendanceapi.security.SecurityService;


@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService){
        this.securityService = securityService;
        createHeader();
        createDrawer();

    }

    private void createDrawer() {
        RouterLink usersLink = new RouterLink("Users", UserView.class);
        usersLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                usersLink,
                new RouterLink("Cards", CardView.class),
                new RouterLink("Activities", ActivityView.class)
                )
        );
    }

    private void createHeader() {
        H1 logo = new H1("Admin");
        logo.addClassName("logo");

        Button logout = new Button("Logout", e -> securityService.logout());
        logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);
        header.addClassName("header");
        header.expand(logo);
        header.setWidth("100%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }
}
