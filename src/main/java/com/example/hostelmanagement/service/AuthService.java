package com.example.hostelmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hostelmanagement.entity.AppUser;
import com.example.hostelmanagement.entity.Student;
import com.example.hostelmanagement.repository.UserRepository;
import com.example.hostelmanagement.repository.StudentRepository;
import com.example.hostelmanagement.security.JwtUtil;
import com.example.hostelmanagement.util.EmailService;

import jakarta.servlet.http.HttpServletRequest;

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

	public void invalidateSession(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Transactional
	public void registerUser(String username, String email, String password) {

		Optional<Student> studentOpt = studentRepository.findByEmail(email);
		
	    if (studentOpt.isEmpty()) {
	        throw new RuntimeException("Use institute ID for registration.");
	    }
		if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User already exists with this email.");
        }

		String encodedPassword = passwordEncoder.encode(password);
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword); // Encrypt password
        AppUser savedUser = userRepository.save(user);
        
        
        Student student = studentOpt.get();
        student.setUser(savedUser);
        studentRepository.save(student);
        
		
	}
}
