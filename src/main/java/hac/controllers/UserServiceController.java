package hac.controllers;

import hac.application.ApplicationConfig;
import hac.Entity.User;
import hac.repositories.UserRepository;
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

    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email).get(0);
        applicationConfig.deleteUser(email);
        userRepository.delete(user);
    }

    public void addUserLogin(String email) {
        User user = userRepository.findByEmail(email).get(0);
        user.addLogin(new Date().toString());
        userRepository.save(user);
    }

    public void updateUserRecoveryPassword(String email, String password) {
        User user = userRepository.findByEmail(email).get(0);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public boolean recoveryPasswordConfirmed(String email, String password) {
        User user = userRepository.findByEmail(email).get(0);
        return passwordEncoder.matches(password, user.getPassword());
    }

    public void updateUserPassword(String email, String password) {
        User user = userRepository.findByEmail(email).get(0);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        applicationConfig.changePassword(email, password);
    }
}
