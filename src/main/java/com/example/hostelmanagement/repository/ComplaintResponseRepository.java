package com.example.hostelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hostelmanagement.entity.ComplaintResponse;

@Repository
public interface ComplaintResponseRepository extends JpaRepository<ComplaintResponse, Long> {

}
