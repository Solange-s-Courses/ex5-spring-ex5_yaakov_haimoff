package hac.services;

import hac.application.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private ApplicationConfig applicationConfig;

    @Autowired
    public ApplicationService(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    /**
     * this method adds a user to the application config security
     * @param username - username
     * @param password - password
     * @param roles - roles
     */
    public void addUser(String username, String password, String... roles) {
        applicationConfig.addUser(username, password, roles);
    }

    /**
     * this method enables or disables a user in the application config security
     * @param username - username
     * @param enableDisable - true or false
     */
    public void enableDisableUser(String username, boolean enableDisable) {
        applicationConfig.enableDisableUser(username, enableDisable);
    }

    /**
     * this method deletes a user from the application config security
     * @param username - username
     */
    public void deleteUser(String username) {
        applicationConfig.deleteUser(username);
    }

    /**
     * this method changes the password of a user in the application config security
     * @param username - username
     * @param newPassword - new password
     */
    public void changePassword(String username, String newPassword) {
        applicationConfig.changePassword(username, newPassword);
    }
}
