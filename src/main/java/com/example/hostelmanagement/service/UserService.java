package com.example.hostelmanagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hostelmanagement.entity.AppUser;
import com.example.hostelmanagement.repository.AdminRepository;
import com.example.hostelmanagement.repository.UserRepository;
import com.example.hostelmanagement.repository.StudentRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private AdminRepository adminRepository;

    public AppUser addUser(AppUser user) {
        return userRepository.save(user);
    }
    
	public Optional<AppUser> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public AppUser findByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	public Object getUserProfile(AppUser userDetails) {
        String role = userDetails.getRole().name();

        if ("STUDENT".equals(role)) {
            return studentRepository.findByUser(userDetails);
        } else if ("ADMIN".equals(role) || "CARETAKER".equals(role)) {
            return adminRepository.findByUser(userDetails);
        }

        return null;
    }
	

}