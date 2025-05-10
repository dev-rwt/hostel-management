package com.example.hostelmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hostelmanagement.entity.Caretaker;
import com.example.hostelmanagement.repository.CaretakerRepository;

@Service
public class CaretakerService {
	@Autowired
	private CaretakerRepository caretakerRepository;
	
	public void addCaretaker(Caretaker caretaker) {
		caretakerRepository.save(caretaker);
	}

	public List<Caretaker> getAllCaretakers() {
		return caretakerRepository.findAll();
	}

}
