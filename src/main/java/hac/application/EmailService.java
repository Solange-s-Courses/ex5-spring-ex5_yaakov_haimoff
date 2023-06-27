package hac.application;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * Sends a simple email message to the specified email
     * address with a randomly generated password.
     * @param to The email address to send the message to.
     * @return The generated password.
     */
    public String sendSimpleMessage(String to) {
        // Generate password
        String password = generatePassword();

        // Append password to the email text
        String emailContent = "Your generated password is: " + password;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo(to);
        message.setSubject("Password Confirmation");
        message.setText(emailContent);
        emailSender.send(message);
        return password;
    }

    /**
     * Generates a random password.
     * @return The generated password.
     */
    private String generatePassword() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();
        // Get the UUID as a string and remove any hyphens
        String password = uuid.toString().replace("-", "");
        // Trim the password to the desired length (e.g., 8 characters)
        password = password.substring(0, 8);
        return password;
    }
}
