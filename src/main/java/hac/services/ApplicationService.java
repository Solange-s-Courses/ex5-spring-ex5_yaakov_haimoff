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

    public void addUser(String username, String password, String... roles) {
        applicationConfig.addUser(username, password, roles);
    }

    public void enableDisableUser(String username, boolean enableDisable) {
        applicationConfig.enableDisableUser(username, enableDisable);
    }

    public void deleteUser(String username) {
        applicationConfig.deleteUser(username);
    }

    public void changePassword(String username, String newPassword) {
        applicationConfig.changePassword(username, newPassword);
    }
}
