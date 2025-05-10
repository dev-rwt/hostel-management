package com.example.hostelmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.hostelmanagement.dto.VerificationToken;
import com.example.hostelmanagement.entity.Admin;
import com.example.hostelmanagement.entity.AppUser;
import com.example.hostelmanagement.entity.Role;
import com.example.hostelmanagement.repository.UserRepository;
import com.example.hostelmanagement.repository.AdminRepository;
import com.example.hostelmanagement.repository.CaretakerRepository;
import com.example.hostelmanagement.repository.StudentRepository;
import com.example.hostelmanagement.security.JwtUtil;
import com.example.hostelmanagement.util.EmailService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CaretakerRepository caretakerRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder; 

    @Autowired
    private EmailService emailService;

    // Store OTPs temporarily in memory with email as the key
    private final Map<String, String> otpStorage = new HashMap<>();

    public void sendOtp(String email) {
        // Generate a 6-digit OTP
        String generatedOTP = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, generatedOTP);
        emailService.sendOtp(email, generatedOTP);
    }

    public boolean verifyOtp(String email, String otp) {
        // Check if OTP exists for the email and matches the input
        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
            otpStorage.remove(email); // Remove OTP after successful verification
            return true;
        }
        return false;
    }
    
    public String authenticate(String email, String password) throws Exception {
        Optional<AppUser> user = userRepository.findByEmail(email);
        
        if (user == null)  {
            throw new Exception("Invalid credentials");
        }
        
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
			throw new Exception("Invalid credentials");
		}

        // Generate a JWT or any other secure token
        return generateToken(user.get());
    }

    private String generateToken(AppUser user) {
    	String token = JwtUtil.generateToken(user.getEmail(), user.getRole());
        return token; // Replace with actual token generation logic
    }

	public HttpServletResponse invalidateSession(HttpServletResponse response) {
		Cookie cookie = new Cookie("token", null); // 👈 must match the name in JwtCookieFilter
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // use only if you're on HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // expire immediately
        response.addCookie(cookie);
		return response;
	}
	
	public boolean isEmailAllowed(String email) {
        // Allow email if there are no users in the repository
        if (userRepository.count() == 0) {
            return true;
        }
        
        System.out.println("Email: " + email);
        // Otherwise, check if the email is not already registered
        return (adminRepository.existsByEmail(email) || caretakerRepository.existsByEmail(email) || studentRepository.existsByEmail(email));
    }

    public void registerVerifiedUser(VerificationToken vt) {
        String email = vt.getEmail();
        String name = vt.getName();
        String password = vt.getPassword();

        // Check if there are no users in the repository
        if (userRepository.count() == 0) {
            // Create an admin user
            AppUser adminUser = new AppUser();
            adminUser.setEmail(email);
            adminUser.setUsername(name);
            adminUser.setPassword(passwordEncoder.encode(password));
            adminUser.setRole(Role.ADMIN);
            userRepository.save(adminUser);

            // Create and associate an Admin object
            Admin admin = new Admin();
            admin.setEmail(email);
            admin.setName(name);
            admin.setUser(adminUser);
            adminRepository.save(admin);
        } else {
            // Normal registration process
            Role role = determineRoleByEmail(email);
            AppUser user = new AppUser();
            user.setEmail(email);
            user.setUsername(name);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            userRepository.save(user);

            assignUserEntityByRole(email, user);
        }
    }

    private Role determineRoleByEmail(String email) {
        if (adminRepository.existsByEmail(email) ) return Role.ADMIN;
        if (caretakerRepository.existsByEmail(email)) return Role.CARETAKER;
        if (studentRepository.existsByEmail(email)) return Role.STUDENT;
        throw new RuntimeException("Email not associated with any role");
    }

    private void assignUserEntityByRole(String email, AppUser user) {
        adminRepository.findByEmail(email).ifPresent(admin -> {
            admin.setUser(user);
            adminRepository.save(admin);
        });
        caretakerRepository.findByEmail(email).ifPresent(caretaker -> {
            caretaker.setUser(user);
            caretakerRepository.save(caretaker);
        });
        studentRepository.findByEmail(email).ifPresent(student -> {
            student.setUser(user);
            studentRepository.save(student);
        });
    }
}
