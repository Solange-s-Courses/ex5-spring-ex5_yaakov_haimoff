package hac.application;

import hac.controllers.UserServiceController;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.Collection;

@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserServiceController userServiceController;

    public LoginSuccessListener(UserServiceController userServiceController) {
        this.userServiceController = userServiceController;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_USER")) {
                String username = auth.getName();
//                userServiceController.addUserLogin(username);
                break;
            }
        }
    }
}