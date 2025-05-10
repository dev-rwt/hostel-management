package com.example.hostelmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.hostelmanagement.entity.Admin;
import com.example.hostelmanagement.entity.AppUser;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	Object findByUser(AppUser userDetails);

	Optional<Admin> findByEmail(String email);

	boolean existsByEmail(String email);
}