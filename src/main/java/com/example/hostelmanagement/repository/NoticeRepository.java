package com.example.hostelmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hostelmanagement.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findAllByOrderByDateDesc();
    
    
    // Find top 5 recent notices for dashboard display
    List<Notice> findTop5ByOrderByDateDesc();
}

