package com.example.hostelmanagement.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.hostelmanagement.entity.Admin;
import com.example.hostelmanagement.entity.AppUser;
import com.example.hostelmanagement.entity.Room;
import com.example.hostelmanagement.entity.Student;
import com.example.hostelmanagement.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public AppUser addUser(@RequestBody AppUser user) {
        return userService.addUser(user);
    }
    
    @GetMapping("/profile/edit")
    public String editProfile(Authentication authentication, Model model) {
    	if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            return "redirect:/login"; // Redirect if not authenticated
        }

        // ✅ Extract user details from JWT claims
        String email = jwt.getClaim("sub");  
        String role = jwt.getClaim("role");

        // ✅ Fetch user from the database
        Optional<AppUser> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return "redirect:/login"; // Redirect if user not found
        }

        AppUser userDetails = optionalUser.get();
        
        

        // ✅ Check role and cast accordingly
        if ("STUDENT".equals(role)) {
        	Student student = (Student) userService.getUserProfile(userDetails);
        	if(student == null) {
        		return "redirect:/login";
        	}
            model.addAttribute("student", student);
            return "edit_profile"; // Render `student_profile.html`
        } 
        else if ("ADMIN".equals(role)) {
        	Admin admin = (Admin) userService.getUserProfile(userDetails);
            model.addAttribute("admin", admin);
            return "edit_profile"; // Render `admin_profile.html`
        }

        return "error"; // Fallback page
    	
    }
    
    
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/profile")
    public String getUserProfile(Authentication authentication, Model model) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            return "redirect:/login"; // Redirect if not authenticated
        }

        // ✅ Extract user details from JWT claims
        String email = jwt.getClaim("sub");  // "sub" (subject) usually stores email
        String role = jwt.getClaim("role");

        // ✅ Fetch user from the database
        Optional<AppUser> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return "redirect:/login"; // Redirect if user not found
        }

        AppUser userDetails = optionalUser.get();
        
        

        // ✅ Check role and cast accordingly
        if ("STUDENT".equals(role)) {
        	Student student = (Student) userService.getUserProfile(userDetails);
        	if(student == null) {
        		return "redirect:/login";
        	}
            model.addAttribute("student", student);
            return "student_profile"; // Render `student_profile.html`
        } 
        else if ("ADMIN".equals(role)) {
        	Admin admin = (Admin) userService.getUserProfile(userDetails);
            model.addAttribute("admin", admin);
            return "admin_profile"; // Render `admin_profile.html`
        }

        return "error"; // Fallback page
    }

}