package com.example.hostelmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.hostelmanagement.entity.AppUser;
import com.example.hostelmanagement.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
	Student findByName(String name);
	
	@Query("SELECT s FROM Student s WHERE s.room IS NULL")
    List<Student> findUnallocatedStudents();

	Optional<Student> findByNameAndRollNumber(String name, String rollNumber);

	Optional<Student> findByEmail(String email);

	Object findByUser(AppUser userDetails);
}