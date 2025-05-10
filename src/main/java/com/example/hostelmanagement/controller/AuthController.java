package com.example.hostelmanagement.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.hostelmanagement.dto.ForgotPasswordRequest;
import com.example.hostelmanagement.dto.LoginRequest;
import com.example.hostelmanagement.dto.RegisterRequest;
import com.example.hostelmanagement.dto.VerificationToken;
import com.example.hostelmanagement.service.AuthService;
import com.example.hostelmanagement.service.UserService;
import com.example.hostelmanagement.service.VerificationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	@Autowired
    private AuthService authService;
	
	@Autowired 
	private VerificationService verificationService;
	


	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request, HttpSession session) {
	    if (!authService.isEmailAllowed(request.getEmail())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email not allowed.");
	    }
	    
	    // Store registration data in session instead of URL params
	    session.setAttribute("registrationUsername", request.getUsername());
	    session.setAttribute("registrationEmail", request.getEmail());
	    
	    // Send OTP
	    verificationService.sendOtp(request.getUsername(), request.getEmail(), request.getPassword());
	    
	    // Return a successful response with the redirect URL
	    Map<String, String> response = new HashMap<>();
	    response.put("redirectUrl", "/auth/verify");
	    return ResponseEntity.ok(response);
	}

	@GetMapping("/verify")
	public String showVerifyPage(HttpSession session, Model model) {
	    // Retrieve data from session
	    String username = (String) session.getAttribute("registrationUsername");
	    String email = (String) session.getAttribute("registrationEmail");
	    
	    // Security check - if no session data, redirect to registration
	    if (username == null || email == null) {
	        return "redirect:/auth/register";
	    }
	    
	    model.addAttribute("username", username);
	    model.addAttribute("email", email);
	    return "verify-otp"; // returns verify.html or verify.jsp
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verifyOtp(HttpSession session, @RequestParam String otp) {
	    // Get email from session rather than URL parameter
	    String email = (String) session.getAttribute("registrationEmail");
	    
	    // Validate session data
	    if (email == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Session expired. Please register again.");
	    }
	    
	    Optional<VerificationToken> vtOpt = verificationService.verifyOtp(email, otp);
	    if (vtOpt.isPresent()) {
	        authService.registerVerifiedUser(vtOpt.get());
	        
	        // Clear sensitive registration data from session
	        session.removeAttribute("registrationUsername");
	        session.removeAttribute("registrationEmail");
	        
	        return ResponseEntity.ok("Email verified. Account created.");
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP.");
	    }
	}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    	try {
            String token = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            response.setHeader("Authorization", token);
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid email or password"));
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        response = authService.invalidateSession(response);
        return "redirect:/auth/login"; // Redirect to login page after logout
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.sendOtp(request.getEmail());
        return ResponseEntity.ok("OTP sent to email.");
    }
}