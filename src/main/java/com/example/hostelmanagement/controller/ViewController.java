package com.example.hostelmanagement.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class ViewController {
	
	@PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    @GetMapping("/")
    public String showDashboard(Model model) {
	    return "index";
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

