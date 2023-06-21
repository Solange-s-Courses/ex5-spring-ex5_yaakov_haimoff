package hac.controllers;

import hac.ApplicationConfig;
import hac.Entity.User;
import hac.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ApplicationConfig applicationConfig;

    @Autowired
    public UserServiceController(UserRepository userRepository, PasswordEncoder passwordEncoder, BCryptPasswordEncoder bCryptPasswordEncoder, ApplicationConfig applicationConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.applicationConfig = applicationConfig;
    }

    public String registerUser(String email, String password) {
        // Check if the email already exists in the repository
        if (!userRepository.findByEmail(email).isEmpty()) {
            return "User already exists";
        }
        // Add the user to the repository and to the applicationConfig
        applicationConfig.addUser(email, password, "USER");
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, true, new Date().toString());
        userRepository.save(user);
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

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean passwordMatches(String email, String enteredPassword) {
        User user = (User) userRepository.findByEmail(email);
        if (user != null) {
            String storedEncodedPassword = user.getPassword();
            return passwordEncoder.matches(enteredPassword, storedEncodedPassword);
        }
        return false;
    }

}
