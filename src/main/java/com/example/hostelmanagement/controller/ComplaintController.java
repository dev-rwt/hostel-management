package com.example.hostelmanagement.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.hostelmanagement.entity.AppUser;
import com.example.hostelmanagement.entity.Complaint;
import com.example.hostelmanagement.entity.ComplaintResponse;
import com.example.hostelmanagement.entity.ComplaintStatus;
import com.example.hostelmanagement.entity.Student;
import com.example.hostelmanagement.service.ComplaintService;
import com.example.hostelmanagement.service.StudentService;
import com.example.hostelmanagement.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@Controller
@RequestMapping("/complaints")
public class ComplaintController {
	
	@Autowired
    private ComplaintService complaintService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StudentService studentService;

    @PostMapping
    public ResponseEntity<Complaint> createComplaint(@RequestBody Complaint complaint, Authentication authentication) {
    	if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // ✅ Extract user details from JWT claims
        String email = jwt.getClaim("sub"); 
        
        return ResponseEntity.status(HttpStatus.CREATED).body(complaintService.createComplaint(complaint,email));
    }

    @GetMapping("/{complaintId}")
    public String getComplaintById(@PathVariable Long complaintId, Model model) {
    	Complaint complaint = complaintService.getComplaintById(complaintId);
    	if (complaint == null) {
			return "error"; 
		}
		System.out.println(complaint);
		// Add the complaint to the model
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		String complaintJson = null;
		try {
			complaintJson = mapper.writeValueAsString(complaint);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		model.addAttribute("complaintJson", complaintJson);
        return "complaint_view";
    }
    
    @PostMapping("/{complaintId}/response")
    public ResponseEntity<ComplaintResponse> submitResponse(@PathVariable Long complaintId, @RequestParam String message, @RequestParam String status, Authentication authentication, Model model) {
        // Fetch the complaint by ID
        Complaint complaint = complaintService.getComplaintById(complaintId);
        if (complaint == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if complaint not found
        }

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if not authenticated
        }

        // ✅ Extract user details from JWT claims
        String email = jwt.getClaim("sub");  
        String role = jwt.getClaim("role");

        // ✅ Fetch user from the database
        Optional<AppUser> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if user not found
        }
		// Check if the user is an admin
        AppUser user = optionalUser.get();
        
        complaint.setStatus(ComplaintStatus.valueOf(status)); 
        complaintService.save(complaint);

        // Create a new ComplaintResponse
        ComplaintResponse response = new ComplaintResponse();
        response.setMessage(message);
        response.setComplaint(complaint);
        response.setUser(user);

        // Save the response
        complaintService.save(response);

        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/my-complaints")
    public String getMyComplaints(Authentication authentication, Model model) {
		if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
			return "redirect:/login"; // Redirect if not authenticated
		}

		// ✅ Extract user details from JWT claims
		String email = jwt.getClaim("sub");  
		String role = jwt.getClaim("role");

		// ✅ Fetch user from the database
		Student student = studentService.getStudentByEmail(email);
		List<Complaint> complaints = complaintService.getComplaintsByStudent(student.getId());
		
		model.addAttribute("complaints", complaints);
		
		return "complaints";
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
    public String getAllComplaints(Model model) {
    	List<Complaint> complaints = complaintService.getAllComplaints();
    	//System.out.println(complaints);
    	model.addAttribute("complaints", complaints);
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

}
