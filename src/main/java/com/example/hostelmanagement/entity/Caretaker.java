package com.example.hostelmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "caretakers")
public class Caretaker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    
    @OneToOne
    @JoinColumn(name = "hostel_id", referencedColumnName = "id", unique = true)
    private Hostel hostel;
	
	@OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private AppUser user;
    

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Hostel getHostel() {
		return hostel;
	}
	public void setHostel(Hostel hostel) {
		this.hostel = hostel;
	}
	public AppUser getUser() {
		return user;
	}
	public void setUser(AppUser user) {
		this.user = user;
	}
    
}