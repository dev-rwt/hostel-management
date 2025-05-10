package com.example.hostelmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hostelmanagement.entity.Admin;
import com.example.hostelmanagement.repository.AdminRepository;

@Service
public class AdminService {
	@Autowired
	private AdminRepository adminRepository;
	
	public void addAdmin(Admin admin) {
		adminRepository.save(admin);
	}
}
