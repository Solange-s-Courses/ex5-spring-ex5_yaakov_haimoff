package hac.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

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


    public User(String email, String password, boolean enabled, String dateRegistered) {
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.dateRegistered = dateRegistered;
        this.recoveryPassword = "";
    }

    // Getters and setters for other attributes
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password of the user
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the state of the user
     * @return enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the state of the user
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns the date of registration of the user
     * @return dateRegistered
     */
    public String getDateRegistered() {
        return dateRegistered;
    }

    /**
     * gets the recovery password of the user
     */
    public String getRecoveryPassword() { return recoveryPassword; }

    /**
     * sets the recovery password of the user
     * @param recoveryPassword
     */
    public void setRecoveryPassword(String recoveryPassword) { this.recoveryPassword = recoveryPassword; }

    public User() {}
}

