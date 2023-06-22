package hac.controllers;

import hac.application.EmailService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MyController {

    private final UserServiceController userServiceController;
    private final EmailService emailService;

    public MyController(UserServiceController userServiceController, EmailService emailService) {
        this.userServiceController = userServiceController;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/admin")
    public String adminIndex(Model model) {
        model.addAttribute("users", userServiceController.getRepo().findAll());
        return "admin/index";
    }

    @GetMapping("/user")
    public String userIndex(Model model) {
        model.addAttribute("role", "hello User!");
        return "user/index";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("message", "Please register!");
        return "register";
    }

    @PostMapping("/registered")
    public String registerUser(Model model, String email, String password) {
        boolean isRegistered = userServiceController.registerUser(email, password);

        if (isRegistered) {
            return "login";
        } else {
            model.addAttribute("message", "User already exists!");
            return "register";
        }
    }

    @GetMapping("/admin/enableDisable")
    public String adminEnableDisable(@RequestParam("email") String userEmail, Model model) {
        userServiceController.setUserEnabledDisabled(userEmail);
        model.addAttribute("users", userServiceController.getRepo().findAll());
        return "admin/index";
    }

    @GetMapping("/admin/deleteUser")
    public String adminDelete(@RequestParam("email") String userEmail, Model model) {
        userServiceController.deleteUser(userEmail);
        model.addAttribute("users", userServiceController.getRepo().findAll());
        return "admin/index";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "forgotPassword";
    }

    @PostMapping("/confirmEmailPswd")
    public String confirmEmailPswd(@RequestParam("email") String userEmail) {
        String password = emailService.sendSimpleMessage(userEmail);
        userServiceController.updateUserRecoveryPassword(userEmail, password);
        return "confirmEmailPswd";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(Model model, @RequestParam("email") String userEmail, @RequestParam("password") String password) {
        if(userServiceController.recoveryPasswordConfirmed(userEmail, password)) {
            return "resetPassword";
        } else {
            return "confirmEmailPswd";
        }
    }

    @PostMapping("/setNewPassword")
    public String setNewPassword(@RequestParam("email") String userEmail, @RequestParam("password") String password) {
        userServiceController.updateUserPassword(userEmail, password);
        return "login";
    }
}
