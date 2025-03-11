package com.example.hostelmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.hostelmanagement.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmailNull(String email);
    
	boolean existsByEmail(String email);
}