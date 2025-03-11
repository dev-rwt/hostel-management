package com.example.hostelmanagement.repository;

import com.example.hostelmanagement.entity.Hostel;
import com.example.hostelmanagement.entity.Student;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HostelRepository extends JpaRepository<Hostel, Long> {
    @Query("SELECT DISTINCT h FROM Hostel h")
    List<Hostel> findAllUnique();

	Optional<Hostel> findByName(String hostelName);
    
    
    

}
