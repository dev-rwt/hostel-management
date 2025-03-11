package com.example.hostelmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hostelmanagement.entity.User;
import com.example.hostelmanagement.repository.AdminRepository;
import com.example.hostelmanagement.repository.StudentRepository;
import com.example.hostelmanagement.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private AdminRepository adminRepository;

    public User addUser(User user) {
        return userRepository.save(user);
    }
    
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Object getUserProfile(User userDetails) {
        String role = userDetails.getRole().name();

        if ("STUDENT".equals(role)) {
            return studentRepository.findByUser(userDetails);
        } else if ("ADMIN".equals(role)) {
            return adminRepository.findByUser(userDetails);
        }

        return null;
    }
	

}