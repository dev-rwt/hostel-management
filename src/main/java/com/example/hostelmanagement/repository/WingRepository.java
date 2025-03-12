package com.example.hostelmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.hostelmanagement.entity.Hostel;
import com.example.hostelmanagement.entity.Wing;

@Repository
public interface WingRepository extends JpaRepository<Wing, Long> {

	Optional<Wing> findByName(String wingName);

	Optional<Wing> findByNameAndHostel(String name, Hostel hostel);
	
	@Query("SELECT w FROM Wing w JOIN w.hostel h WHERE h.name = :hostelName")
	List<Wing> findByHostelName(String hostelName);
	
	@Query("SELECT w FROM Wing w JOIN w.hostel h WHERE h.id = :id")
	List<Wing> findByHostelId(Long id);  

}
