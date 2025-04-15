package com.example.hostelmanagement.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Entity
@Data
public class Complaint {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    

    @Enumerated(EnumType.STRING)
    private ComplaintStatus status = ComplaintStatus.OPEN;

    @Enumerated(EnumType.STRING)
    private ComplaintPriority priority = ComplaintPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    private ComplaintCategory category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    private Room room;
    
    
    @ManyToOne
    @JoinColumn(name = "hostel_id")
    private Hostel hostel;
    
    
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = true)
    private Admin admin;
    
    
    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ComplaintResponse> responses;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
    	updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() {
    	return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ComplaintStatus getStatus() {
		return status;
	}

	public void setStatus(ComplaintStatus status) {
		this.status = status;
	}

	public ComplaintPriority getPriority() {
		return priority;
	}

	public void setPriority(ComplaintPriority priority) {
		this.priority = priority;
	}

	public ComplaintCategory getCategory() {
		return category;
	}

	public void setCategory(ComplaintCategory category) {
		this.category = category;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Hostel getHostel() {
		return hostel;
	}

	public void setHostel(Hostel hostel) {
		this.hostel = hostel;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public List<ComplaintResponse> getResponses() {
		return responses;
	}

	public void setResponses(List<ComplaintResponse> responses) {
		this.responses = responses;
	}

    
}

