package hac.application;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String LOGIN_ENDPOINT = "/user/login";

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_USER")) {
                String username = auth.getName();
//                sendLoginRequest(username);
                break;
            }
        }
    }

    private void sendLoginRequest(String username) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SERVER_URL + LOGIN_ENDPOINT;
        restTemplate.postForObject(url, null, String.class, username);
    }
}
