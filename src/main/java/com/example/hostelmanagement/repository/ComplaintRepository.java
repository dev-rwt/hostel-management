package com.example.hostelmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.hostelmanagement.entity.Complaint;
import com.example.hostelmanagement.entity.ComplaintCategory;
import com.example.hostelmanagement.entity.ComplaintStatus;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
	@Query("SELECT c FROM Complaint c WHERE c.student.id = ?1")
    List<Complaint> findByStudentId(Long studentId);
    List<Complaint> findByStatus(ComplaintStatus status);
    List<Complaint> findByCategory(ComplaintCategory category);
    List<Complaint> findByStatusAndCategory(ComplaintStatus status, ComplaintCategory category);
}

