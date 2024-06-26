package hac.services;

import hac.Entity.User;
import hac.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class UserServiceController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param email - email
     * @param password - password
     * @return true if user registered successfully, false if error occurred
     */
    public boolean registerUser(String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, true, new Date().toString());
        try {
            userRepository.save(user);
            return true; // User registered successfully
        } catch (Exception e) {
            return false; // Error occurred, user not registered
        }
    }

    // get users
    public UserRepository getRepo() {
        return userRepository;
    }

    /**
     * @param prefix - prefix
     * @return true if user exists with the given prefix, false if not
     */
    public Page<User> getUsersWithEmailStartingWith(String prefix, Pageable pageable) {
        return userRepository.findByEmailStartingWith(prefix, pageable);
    }


    /**
     * @param email - email
     * this method is used to enable/disable user
     * @return the new state of the user
     */
    public boolean setUserEnabledDisabled(String email) {
        User user = userRepository.findByEmail(email).get(0);
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
        return !user.isEnabled();
    }

    /**
     * @param email - email
     * this method is used to delete user
     */
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email).get(0);
        userRepository.delete(user);
    }

    /**
     * @param email - email
     * @param password - password
     * this method is used to change user password
     */
    public void updateUserRecoveryPassword(String email, String password) {
        User user = userRepository.findByEmail(email).get(0);
        user.setRecoveryPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public boolean recoveryPasswordConfirmed(String email, String password) {
        User user = userRepository.findByEmail(email).get(0);
        return passwordEncoder.matches(password, user.getRecoveryPassword());
    }

    public void updateUserPassword(String email, String password) {
        User user = userRepository.findByEmail(email).get(0);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
