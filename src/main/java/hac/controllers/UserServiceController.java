package hac.controllers;

import hac.ApplicationConfig;
import hac.Entity.User;
import hac.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final ApplicationConfig applicationConfig;

    @Autowired
    public UserServiceController(UserRepository userRepository, PasswordEncoder passwordEncoder, ApplicationConfig applicationConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationConfig = applicationConfig;
    }

    public String registerUser(String email, String password) {
        System.out.println("email: " + email);
        // Check if the email already exists in the repository
        if (userRepository.findByEmail(email) != null) {
            return "User already exists";
        }
        // Register the user dynamically
        applicationConfig.addUser(email, password, "USER");
        System.out.println("email does not exist");
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword);
        System.out.println("saving user");
        userRepository.save(user);
        return "User registered successfully";
    }
}
