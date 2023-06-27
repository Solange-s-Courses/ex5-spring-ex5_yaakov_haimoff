package hac.application;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import java.io.IOException;
import java.util.Collection;

public class RoleBasedAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * This method is used to redirect the user to different pages after login based on their role.
     * @param request - HttpServletRequest
     * @param response - HttpServletResponse
     * @param authentication - Authentication
     * @throws IOException - IOException
     * @throws ServletException - ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                setDefaultTargetUrl("/admin");
                break;
            } else if (authority.getAuthority().equals("ROLE_USER")) {
                setDefaultTargetUrl("/user");
                break;
            }
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}