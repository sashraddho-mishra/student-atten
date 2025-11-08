package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        // If user is authenticated, they will be forwarded to /teacher/dashboard by security after login.
        // For direct GET / requests, redirect to login (security will handle authenticated users).
        return "redirect:/login";
    }
}
