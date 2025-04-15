package com.example.hostelmanagement.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity
public class ComplaintResponse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "complaint_id")
    @JsonBackReference
    private Complaint complaint;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user; 

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
    
    public Long getId() {
		return id;
	}
    
    public String getMessage() {
    	return message;
    }
    
    public void setMessage(String message) {
		this.message = message;
	}

	public Complaint getComplaint() {
		return complaint;
	}

	public void setComplaint(Complaint complaint) {
		this.complaint = complaint;
	}
		
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

    
    
    

    
}
