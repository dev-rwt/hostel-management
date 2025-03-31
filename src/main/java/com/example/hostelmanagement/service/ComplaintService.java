package com.example.hostelmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hostelmanagement.entity.Admin;
import com.example.hostelmanagement.entity.Complaint;
import com.example.hostelmanagement.entity.ComplaintCategory;
import com.example.hostelmanagement.entity.ComplaintResponse;
import com.example.hostelmanagement.entity.ComplaintStatus;
import com.example.hostelmanagement.entity.Student;
import com.example.hostelmanagement.repository.AdminRepository;
import com.example.hostelmanagement.repository.ComplaintRepository;
import com.example.hostelmanagement.repository.ComplaintResponseRepository;
import com.example.hostelmanagement.repository.StudentRepository;

@Service
public class ComplaintService {
	
	@Autowired
    private ComplaintRepository complaintRepository;
	
	@Autowired
    private StudentRepository studentRepository;
	
	@Autowired
    private AdminRepository adminRepository;
	
	@Autowired
    private ComplaintResponseRepository responseRepository;

    public Complaint createComplaint(Complaint complaint) {
        Student student = studentRepository.findById(complaint.getStudent().getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        complaint.setStatus(ComplaintStatus.OPEN);
        complaint.setStudent(student);

        return complaintRepository.save(complaint);
    }

    public Complaint getComplaintById(Long complaintId) {
        return complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
    }

    public List<Complaint> getComplaintsByStudent(Long studentId) {
        return complaintRepository.findByStudentId(studentId);
    }

    public List<Complaint> getAllComplaints(ComplaintStatus status, ComplaintCategory category) {
        if (status != null && category != null) {
            return complaintRepository.findByStatusAndCategory(status, category);
        } else if (status != null) {
            return complaintRepository.findByStatus(status);
        } else if (category != null) {
            return complaintRepository.findByCategory(category);
        } else {
            return complaintRepository.findAll();
        }
    }

    public Complaint updateComplaintStatus(Long complaintId, ComplaintStatus status) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setStatus(status);
        return complaintRepository.save(complaint);
    }

    public Complaint assignComplaint(Long complaintId, Long adminId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        complaint.setAdmin(admin);
        return complaintRepository.save(complaint);
    }

    public ComplaintResponse addResponse(Long complaintId, ComplaintResponse response) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        response.setComplaint(complaint);
        return responseRepository.save(response);
    }
}

