package com.example.hostelmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.hostelmanagement.entity.Admin;
import com.example.hostelmanagement.entity.Student;
import com.example.hostelmanagement.entity.User;
import com.example.hostelmanagement.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }
    
    @GetMapping("/profile")
    public String getUserProfile(@AuthenticationPrincipal User userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login"; // Redirect if not authenticated
        }

        String role = userDetails.getRole().name();
        Object profile = userService.getUserProfile(userDetails);

        if ("STUDENT".equals(role) && profile instanceof Student student) {
            model.addAttribute("studentProfile", student);
            return "student_profile"; // Redirect to `student_profile.html`
        } else if ("ADMIN".equals(role) && profile instanceof Admin admin) {
            model.addAttribute("adminProfile", admin);
            return "admin_profile"; // Redirect to `admin_profile.html`
        }

        return "error"; // Fallback page if something goes wrong
    }
}