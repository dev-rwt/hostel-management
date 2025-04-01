package com.example.hostelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.hostelmanagement.entity.Admin;
import com.example.hostelmanagement.entity.AppUser;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	Object findByUser(AppUser userDetails);
	
	@Query("SELECT a FROM Admin a WHERE a.hostel.id = :id")
	Admin findByHostelId(Long id);
}