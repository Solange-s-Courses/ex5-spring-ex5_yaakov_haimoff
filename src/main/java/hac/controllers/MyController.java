package hac.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

@Controller
public class MyController {

    private final UserServiceController userServiceController;

    public MyController(UserServiceController userServiceController) {
        this.userServiceController = userServiceController;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("greeting", "Hello World");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/registered")
    public String registerUser(Model model, String email, String password) {
        String message = userServiceController.registerUser(email, password);
        if(Objects.equals(message, "User registered successfully"))
            return "login";
        else{
            model.addAttribute("message", "Error,Please register again!");
            return "register";
        }
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("message", "Please register!");
        return "register";
    }
}
