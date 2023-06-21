package hac.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@RequestMapping("/")
public class MyController {

    private final UserServiceController userServiceController;

    public MyController(UserServiceController userServiceController) {
        this.userServiceController = userServiceController;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("role", "hello!");
        return "index";
    }

    @GetMapping("/user")
    public String userIndex(Model model) {
        model.addAttribute("role", "hello User!");
        return "user/index";
    }

    @GetMapping("/admin")
    public String adminIndex(Model model) {
        model.addAttribute("users", userServiceController.getRepo().findAll());
        return "admin/index";
    }

    @GetMapping("/admin/enableDisable")
    public String adminEnableDisable(@RequestParam("email") String userEmail, Model model) {
        // set user enabled to true
        userServiceController.setUserEnabledDisabled(userEmail);
        model.addAttribute("users", userServiceController.getRepo().findAll());
        return "admin/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/registered")
    public String registerUser(Model model, String email, String password) {
        String message = userServiceController.registerUser(email, password);
        if (Objects.equals(message, "User registered successfully"))
            return "login";
        else {
            model.addAttribute("message", "User already exists!");
            return "register";
        }
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("message", "Please register!");
        return "register";
    }
}
