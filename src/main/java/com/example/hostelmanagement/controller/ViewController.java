package com.example.hostelmanagement.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.hostelmanagement.entity.Admin;
import com.example.hostelmanagement.entity.AppUser;
import com.example.hostelmanagement.entity.Complaint;
import com.example.hostelmanagement.entity.Notice;
import com.example.hostelmanagement.entity.Student;
import com.example.hostelmanagement.repository.NoticeRepository;
import com.example.hostelmanagement.service.ComplaintService;
import com.example.hostelmanagement.service.UserService;



@Controller
public class ViewController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private NoticeRepository noticeRepository;
	
	@PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    @GetMapping("/")
    public String showDashboard(Model model, Authentication authentication) {
		if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            return "redirect:/login"; // Redirect if not authenticated
        }

        // ✅ Extract user details from JWT claims
        String email = jwt.getClaim("sub");  
        String role = jwt.getClaim("role");

        // ✅ Fetch user from the database
        Optional<AppUser> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return "redirect:/login"; // Redirect if user not found
        }

        AppUser userDetails = optionalUser.get();
		List<Notice> recentNotices = noticeRepository.findTop5ByOrderByDateDesc();
        model.addAttribute("recentNotices", recentNotices);
        
        
        
        

        // ✅ Check role and cast accordingly
        if ("STUDENT".equals(role)) {
        	Student student = (Student) userService.getUserProfile(userDetails);
        	if(student == null) {
        		return "redirect:/login";
        	}
            model.addAttribute("student", student);
            List<Complaint> studentComplaints = complaintService.getComplaintsByStudent(student.getId());
            model.addAttribute("studentComplaints", studentComplaints);
            return "dashboard_student"; // Render `student_profile.html`
        } 
        else if ("ADMIN".equals(role) || "CARETAKER".equals(role)) {
        	Admin admin = (Admin) userService.getUserProfile(userDetails);
            model.addAttribute("admin", admin);
            return "dashboard_admin"; // Render `admin_profile.html`
        }

        return "error"; // Fallback page
    }
	
	
	

    @GetMapping("/auth/login")
    public String showLoginPage() {
        return "login";  
    }
    
    @GetMapping("/auth/register")
    public String showRegisterPage() {
        return "register";  
    }


    
    @GetMapping("/students/upload")
    public String showUploadPage() {
		return "upload";
	}
    
    @GetMapping("/internship")
    public String showInternshipForm() {
		return "internship_form";
	}
    
    @GetMapping("/internship/view")
    public String showAllInternshipForm() {
		return "view_internship";
	}
    
    @GetMapping("user/profile")
    public String showProfile() {
    			return "student_profile";
    }



    @GetMapping("/applications")
    public String showApplications() {
        return "applications";
    }

    @GetMapping("/application/{id}/status")
    public String showApplicationStatus() {
        return "application_status";
    }

    @GetMapping("/application/{id}/update")
    public String updateApplication() {
        return "application_update";
    }

    @GetMapping("/payment/update")
    public String updatePayment() {
        return "payment_update";
    }

    @GetMapping("/circulars")
    public String showCirculars() {
        return "circulars";
    }

    @GetMapping("/notifications/email")
    public String sendEmailNotification() {
        return "notifications_email";
    }

    @GetMapping("/reports/pdf")
    public String generatePdfReport() {
        return "reports_pdf";
    }
}

