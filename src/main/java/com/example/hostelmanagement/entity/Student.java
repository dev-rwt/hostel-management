package com.example.hostelmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String rollNumber;
    private String gender;
    private String phoneNo;
    @Column(unique = true, nullable = false)
    private String email;
    
    private int year; 

    @Enumerated(EnumType.STRING)
    private Program prog;

    @Enumerated(EnumType.STRING)
    private Department dept;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    @JsonIgnore
    private AppUser user;
    
	@ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
	@JsonIgnore
    private Room room;
	
	@JsonProperty("room")
    public Long getRoomId() {
        return (room != null) ? room.getId() : null;
    }
	

	public Long getId() {
		return id;
	}



	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getRollNumber() {
		return rollNumber;
	}


	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
	}


	public String getPhoneNo() {
		return phoneNo;
	}


	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}


	public Room getRoom() {
		return room;
	}


	public void setRoom(Room room) {
		this.room = room;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public AppUser getUser() {
		return user;
	}


	public void setUser(AppUser user) {
		this.user = user;
	}




	public int getYear() {
		return year;
	}




	public void setYear(int year) {
		this.year = year;
	}




	public Program getProg() {
		return prog;
	}




	public void setProg(Program prog) {
		this.prog = prog;
	}




	public Department getDept() {
		return dept;
	}




	public void setDept(Department dept) {
		this.dept = dept;
	}
    

    
}