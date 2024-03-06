package sk.stuba.fei.uim.dp.attendanceapi.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {
    public UserDetails getAuthenticatedUser(){
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails){
            return (UserDetails) context.getAuthentication().getPrincipal();
        }
        return null;
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation("/login");
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null
        );
    }
}
