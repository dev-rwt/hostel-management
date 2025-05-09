package com.example.hostelmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    
	
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
	public void setUser(AppUser savedUser) {
		this.user = savedUser;
		
	}
	public AppUser getUser() {
		return user;
	}
    
}