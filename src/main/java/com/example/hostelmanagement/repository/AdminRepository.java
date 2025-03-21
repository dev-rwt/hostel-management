package com.example.hostelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hostelmanagement.entity.Admin;
import com.example.hostelmanagement.entity.User;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	Object findByUser(User userDetails);
}