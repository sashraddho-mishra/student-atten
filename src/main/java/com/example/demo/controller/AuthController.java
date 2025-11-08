package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam Map<String, String> body, Model model) {
        String username = body.get("username");
        String password = body.get("password");
        String confirm = body.get("confirm");
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        Integer age = null;
        try { age = body.get("age") != null && !body.get("age").isBlank() ? Integer.parseInt(body.get("age")) : null; } catch (NumberFormatException ignored) {}
        String phone = body.get("phone");
        if (username == null || password == null || confirm == null || !password.equals(confirm)) {
            model.addAttribute("error", "Password and confirm password must match and not be empty");
            return "register";
        }

        try {
            userService.registerTeacher(username, password, firstName, lastName, age, phone);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        return "redirect:/login";
    }
}
