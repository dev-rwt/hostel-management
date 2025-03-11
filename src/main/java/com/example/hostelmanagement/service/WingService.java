package com.example.hostelmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hostelmanagement.entity.Wing;
import com.example.hostelmanagement.repository.WingRepository;

@Service
public class WingService {
	
	@Autowired
	private WingRepository wingRepository;

	public List<Wing> getAllWings() {
		return wingRepository.findAll();
	}

	public List<Wing> getWingsByHostel(String hostelName) {
		return wingRepository.findByHostelName(hostelName);
	}

}
