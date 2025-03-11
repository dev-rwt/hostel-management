package com.example.hostelmanagement.service;

import com.example.hostelmanagement.entity.InternshipForm;
import com.example.hostelmanagement.repository.InternshipFormRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InternshipFormService {

    @Autowired
	private InternshipFormRepository internshipFormRepository;

    public void saveInternshipForm(InternshipForm internshipForm) {
        internshipFormRepository.save(internshipForm);
    }

    public List<InternshipForm> getAllForms() {
        return internshipFormRepository.findAll();
    }

	public InternshipForm getFormById(Long id) {
		return internshipFormRepository.findById(id).get();
	}

	public byte[] getOfficialLetterById(Long id) {
		return internshipFormRepository.findById(id).get().getOfficialLetter();
	}

	public byte[] getIdCardById(Long id) {
		return internshipFormRepository.findById(id).get().getIdCard();
	}

	public void updateStatus(Long id, String status) {
		InternshipForm internshipForm = internshipFormRepository.findById(id).get();
		internshipForm.setStatus(status);
		internshipFormRepository.save(internshipForm);
		
	}
}
