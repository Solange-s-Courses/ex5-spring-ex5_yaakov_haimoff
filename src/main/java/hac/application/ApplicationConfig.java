package hac.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Properties;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ApplicationConfig {

    private InMemoryUserDetailsManager manager;

    @Autowired
    public ApplicationConfig() { this.manager = new InMemoryUserDetailsManager(); }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler roleBasedAuthenticationSuccessHandler() {
        return new RoleBasedAuthenticationSuccessHandler();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("static/User_Icon.png");
    }

    /**
     * userDetailsService - UserDetailsService
     * this method initialize the admin
     * @param bCryptPasswordEncoder - PasswordEncoder
     * @return - UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder bCryptPasswordEncoder) {
        manager.createUser(User.withUsername("yaakovhaimoff")
                .password(bCryptPasswordEncoder.encode("Yh160716"))
                .roles("ADMIN")
                .build());
        return manager;
    }

    /**
     * this method initialize the mail service for the application
     * getJavaMailSender - JavaMailSender
     * @return - JavaMailSender
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("ex5springyaakovhaimoff@gmail.com");
        mailSender.setPassword("pgxd qvbr rfiy dbyl");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    /**
     * this method sets the filter chain for the application
     * @param http - HttpSecurity
     * @param authenticationSuccessHandler - AuthenticationSuccessHandler
     * @return - SecurityFilterChain
     * @throws Exception - Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
        http
                .cors(withDefaults())
                .csrf(withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/login", "about", "/signup", "/registered",
                                "/forgotPassword", "/confirmEmailPswd", "/resetPassword", "/setNewPassword",
                                "/403", "/errorpage"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/shared/**").hasAnyRole("USER", "ADMIN"))
                .formLogin((form) -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler) // Set custom authentication success handler
                        .permitAll())
                .logout((logout) -> logout.permitAll())
                .exceptionHandling(
                        (exceptionHandling) -> exceptionHandling
                                .accessDeniedPage("/403"));
        return http.build();
    }

    /**
     * addUser - void
     * this method adds users to the application manager security
     * @param username - String
     * @param password - String
     * @param roles - String...
     */
    public void addUser(String username, String password, String... roles) {
        String encodedPassword = passwordEncoder().encode(password);
        UserDetails user = User.withUsername(username)
                .password(encodedPassword)
                .roles(roles)
                .build();
        manager.createUser(user);
    }

    /**
     * deleteUser - void
     * this method deletes users from the application manager security
     * @param username - String
     */
    public void deleteUser(String username) {
        manager.deleteUser(username);
    }

    /**
     * enableDisableUser - void
     * this method enable or disable users from the application manager security
     * @param username - String
     * @param enableDisable - boolean
     */
    public void enableDisableUser(String username, boolean enableDisable) {
        UserDetails user = manager.loadUserByUsername(username);
        User updatedUser = (User) User.withUserDetails(user)
                .disabled(enableDisable)
                .build();
        manager.updateUser(updatedUser);
    }

    /**
     * changePassword - void
     * this method changes the password of the user in the application manager security
     * @param username - String
     * @param newPassword - String
     */
    public void changePassword(String username, String newPassword) {
        String encodedPassword = passwordEncoder().encode(newPassword);
        UserDetails user = User.withUsername(username)
                .password(encodedPassword)
                .roles("USER")
                .build();
        manager.updateUser(user);
    }
}
