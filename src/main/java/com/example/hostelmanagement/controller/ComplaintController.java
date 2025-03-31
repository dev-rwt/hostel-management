package com.example.hostelmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.hostelmanagement.entity.Complaint;
import com.example.hostelmanagement.entity.ComplaintResponse;
import com.example.hostelmanagement.entity.ComplaintStatus;
import com.example.hostelmanagement.service.ComplaintService;

@Controller
@RequestMapping("/complaints")
public class ComplaintController {
	
	@Autowired
    private ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<Complaint> createComplaint(@RequestBody Complaint complaint) {
        return ResponseEntity.status(HttpStatus.CREATED).body(complaintService.createComplaint(complaint));
    }

    @GetMapping("/{complaintId}")
    public ResponseEntity<Complaint> getComplaintById(@PathVariable Long complaintId) {
        return ResponseEntity.ok(complaintService.getComplaintById(complaintId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Complaint>> getComplaintsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(complaintService.getComplaintsByStudent(studentId));
    }
    
    @GetMapping("/form")
    public String submitComplaint() {
		return "complaint_form";
	}

    @GetMapping
    public String getAllComplaints() {
        return "complaints";
    }

    @PutMapping("/{complaintId}/status")
    public ResponseEntity<Complaint> updateComplaintStatus(
            @PathVariable Long complaintId, 
            @RequestParam ComplaintStatus status) {
        return ResponseEntity.ok(complaintService.updateComplaintStatus(complaintId, status));
    }

    @PutMapping("/{complaintId}/assign/{adminId}")
    public ResponseEntity<Complaint> assignComplaint(@PathVariable Long complaintId, @PathVariable Long adminId) {
        return ResponseEntity.ok(complaintService.assignComplaint(complaintId, adminId));
    }

    @PostMapping("/{complaintId}/responses")
    public ResponseEntity<ComplaintResponse> addResponse(
            @PathVariable Long complaintId, 
            @RequestBody ComplaintResponse response) {
        return ResponseEntity.status(HttpStatus.CREATED).body(complaintService.addResponse(complaintId, response));
    }
}
