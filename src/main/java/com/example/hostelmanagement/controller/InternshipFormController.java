package com.example.hostelmanagement.controller;

import com.example.hostelmanagement.entity.InternshipForm;
import com.example.hostelmanagement.service.InternshipFormService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internship")
public class InternshipFormController {
	
	@Autowired
    private InternshipFormService internshipFormService;
	

    @PostMapping("/submit")
    public ResponseEntity<String> submitForm(
            @RequestParam("name") String studentName,
            @RequestParam("gender") String gender,
            @RequestParam("affiliation") String affiliation,
            @RequestParam("email") String email,
            @RequestParam("contact") String contactNumber,
            @RequestParam("address") String address,
            @RequestParam("mentor") String facultyMentor,
            @RequestParam("mentorEmail") String facultyEmail,
            @RequestParam("departure") String departureDate,
            @RequestParam("arrival") String arrivalDate,
            @RequestParam("remarks") String remarks,
            @RequestParam("idCard") MultipartFile idCard,
            @RequestParam("officialLetter") MultipartFile officialLetter
    ) throws IOException {
        InternshipForm internshipForm = new InternshipForm();
        internshipForm.setName(studentName);
        internshipForm.setGender(gender);
        internshipForm.setAffiliation(affiliation);
        internshipForm.setEmail(email);
        internshipForm.setContact(contactNumber);
        internshipForm.setAddress(address);
        internshipForm.setMentor(facultyMentor);
        internshipForm.setMentorEmail(facultyEmail);
        internshipForm.setDepartureDate(departureDate);
        internshipForm.setArrivalDate(arrivalDate);
        internshipForm.setRemarks(remarks);

        // Convert files to byte arrays
        internshipForm.setIdCard(idCard.getBytes());
        internshipForm.setOfficialLetter(officialLetter.getBytes());

        internshipFormService.saveInternshipForm(internshipForm);
        return ResponseEntity.status(302)
                .location(URI.create("http://localhost:8080/internship/view"))
                .build();
    }
    
    @GetMapping("/document/{id}/idCard")
    public ResponseEntity<ByteArrayResource> getIdCard(@PathVariable Long id) {
        byte[] document = internshipFormService.getIdCardById(id);
        return getDocumentResponse(document, "id_card.pdf");
    }

    // 3. Serve Official Letter Document
    @GetMapping("/document/{id}/officialLetter")
    public ResponseEntity<ByteArrayResource> getOfficialLetter(@PathVariable Long id) {
        byte[] document = internshipFormService.getOfficialLetterById(id);
        return getDocumentResponse(document, "official_letter.pdf");
    }

    // Utility method to return a file as a response
    private ResponseEntity<ByteArrayResource> getDocumentResponse(byte[] document, String filename) {
        if (document == null || document.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ByteArrayResource resource = new ByteArrayResource(document);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/all")
    public List<InternshipForm> getAllForms() {
        return internshipFormService.getAllForms();
    }
    
    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateForm(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
    	String status = requestBody.get("status"); 
    	if (status == null || status.isEmpty()) {
            return ResponseEntity.badRequest().body("Status is required");
        }
    	internshipFormService.updateStatus(id, status);
		return ResponseEntity.ok("Form updated successfully!");
	}
			
}
