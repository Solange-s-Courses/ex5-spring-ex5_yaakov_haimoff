package hac;

import org.springframework.beans.factory.annotation.Autowired;
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

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ApplicationConfig {

    private InMemoryUserDetailsManager manager;

    // make a constructor for the class
    public ApplicationConfig() {
        this.manager = new InMemoryUserDetailsManager();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder bCryptPasswordEncoder) {
        manager.createUser(User.withUsername("admin")
                .password(bCryptPasswordEncoder.encode("password"))
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
                        .requestMatchers("/403","/signup","/registered", "/errorpage").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/**", "/user/**").hasRole("USER")
                        .requestMatchers("/shared/**").hasAnyRole("USER", "ADMIN")
                )
                .formLogin((form) -> form
                                .loginPage("/login")
//                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/", true)
//                                .failureUrl("/")
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


    // instead of defining open path in the method above you can do it here:
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("User_Icon.png");
    }

    // Method to dynamically add users
    public void addUser(String username, String password, String... roles) {
        String encodedPassword = passwordEncoder().encode(password);
        UserDetails user = User.withUsername(username)
                .password(encodedPassword)
                .roles(roles)
                .build();
        manager.createUser(user);
    }


    // Method to dynamically remove a user
    public void removeUser(String username) {
        manager.deleteUser(username);
    }
}
