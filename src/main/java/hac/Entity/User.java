package hac.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotEmpty(message = "Email is mandatory")
    private String email;

    @NotEmpty(message = "Password is mandatory")
    private String password;

    private boolean enabled;

    private String dateRegistered;

    private String recoveryPassword;

    @ElementCollection
    private List<String> logins = new ArrayList<>();

    public User(String email, String password, boolean enabled, String dateRegistered) {
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.dateRegistered = dateRegistered;
        this.recoveryPassword = "";
    }

    // Getters and setters for other attributes

    public List<String> getLogins() {
        return logins;
    }

    public void setLogins(List<String> logins) {
        this.logins = logins;
    }

    public void addLogin(String login) {
        logins.add(login);
    }

    public void removeLogin(String login) {
        logins.remove(login);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public String getRecoveryPassword() { return recoveryPassword; }

    public void setRecoveryPassword(String recoveryPassword) { this.recoveryPassword = recoveryPassword; }

    public User() {}
}

