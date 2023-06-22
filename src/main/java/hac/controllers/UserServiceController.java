package hac.controllers;

import hac.ApplicationConfig;
import hac.Entity.User;
import hac.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationConfig applicationConfig;

    @Autowired
    public UserServiceController(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 ApplicationConfig applicationConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationConfig = applicationConfig;
    }

    public boolean registerUser(String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, true, new Date().toString());
        try {
            userRepository.save(user);
            applicationConfig.addUser(email, password, "USER");
            return true; // User registered successfully
        } catch (Exception e) {
            return false; // Error occurred, user not registered
        }
    }

    // get users
    public UserRepository getRepo() {
        return userRepository;
    }

    public void setUserEnabledDisabled(String email) {
        User user = userRepository.findByEmail(email).get(0);
        user.setEnabled(!user.isEnabled());
        applicationConfig.enableDisableUser(email, !user.isEnabled());
        userRepository.save(user);
    }
}
