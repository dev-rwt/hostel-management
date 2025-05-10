package com.example.hostelmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hostelmanagement.entity.AppUser;
import com.example.hostelmanagement.entity.Caretaker;

public interface CaretakerRepository extends JpaRepository<Caretaker, Long> {

	Optional<Caretaker> findByEmail(String email);

	Object findByUser(AppUser userDetails);

	boolean existsByEmail(String email);

	Caretaker findByHostelId(Long id);
}
