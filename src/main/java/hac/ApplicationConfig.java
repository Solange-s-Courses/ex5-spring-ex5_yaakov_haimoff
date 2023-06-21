package hac;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ApplicationConfig {

    private InMemoryUserDetailsManager manager;

    private SimpleUrlAuthenticationFailureHandler failureHandler;

    // make a constructor for the class
    public ApplicationConfig() {
        this.manager = new InMemoryUserDetailsManager();
        this.failureHandler = new SimpleUrlAuthenticationFailureHandler("/loginPost");
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder bCryptPasswordEncoder) {
        manager.createUser(User.withUsername("yaakovhaimoff")
                .password(bCryptPasswordEncoder.encode("Yh160716"))
                .roles("ADMIN")
                .build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(withDefaults())

                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/403", "/", "/login", "/signup", "/registered", "/errorpage").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/shared/**").hasAnyRole("USER", "ADMIN")
                )
                .formLogin((form) -> form
                                .loginPage("/login")
                                .defaultSuccessUrl("/", true)
//                                .failureForwardUrl("/login")
//                                .failureHandler(customAuthenticationFailureHandler())
                                .permitAll()
                )
                .logout((logout) -> logout.permitAll())
                .exceptionHandling(
                        (exceptionHandling) -> exceptionHandling
                                .accessDeniedPage("/403")
                )

        ;

        return http.build();

    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return failureHandler;
    }


    // instead of defining open path in the method above you can do it here:
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("static/User_Icon.png");
    }

    // Method to dynamically add users
    public void addUser(String username, String password, String... roles) {
        String encodedPassword = passwordEncoder().encode(password);
        UserDetails user = User.withUsername(username)
                .password(encodedPassword)
                .roles(roles)
                .build();
        manager.createUser(user);
        System.out.println("User " + username + " was added successfully");
    }


    // Method to dynamically remove a user
    public void removeUser(String username) {
        manager.deleteUser(username);
    }

    public void enableDisableUser(String username, boolean enableDisable) {
        UserDetails user = manager.loadUserByUsername(username);
        User updatedUser = (User) User.withUserDetails(user)
                .disabled(enableDisable)
                .build();
        manager.updateUser(updatedUser);
        System.out.println("User " + username + " disabled successfully");
    }
}
