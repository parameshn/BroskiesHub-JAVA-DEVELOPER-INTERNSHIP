package com.bh.learnsphere.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }

    @GetMapping("/courses")
    public String courses() {
        return "courses";
    }

    @GetMapping("/course/{id}")
    public String courseDetail() {
        return "course-detail";
    }

    @GetMapping("/quiz/{id}")
    public String quiz() {
        return "quiz";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
}
