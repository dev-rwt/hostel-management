package com.example.hostelmanagement.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;
    
    @Column(name = "floor", nullable = false)
    private int floor;
    
    @ManyToOne
    @JoinColumn(name = "wing_id")
    @JsonIgnore
    private Wing wing;

    @ManyToOne
    @JoinColumn(name = "hostel_id")
    @JsonIgnore
    private Hostel hostel;
    
    private int capacity;
    

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Student> students;
    
    @JsonProperty("hostel")
    public String getHostelName() {
		return (hostel != null) ? hostel.getName() : null;
	}
    
    @JsonProperty("wing")
	public String getWingName() {
		return (wing != null) ? wing.getName() : null;
	}
	
	@JsonProperty("students")
	public List<Long> getStudentIds() {
		return students.stream().map(Student::getId).toList();
	}
	
	public Room() {
		// Default constructor
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Hostel getHostel() {
		return hostel;
	}

	public void setHostel(Hostel hostel) {
		this.hostel = hostel;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}
	
	public void addStudent(Student student) {
		students.add(student);
	}

	public Long getId() {
		return id;
	}
	
	public int getFloor() {
		return floor;
	}
	
	public void setFloor(int floor) {
		this.floor = floor;
	}
	
	public Wing getWing() {
		return wing;
	}
	
	public void setWing(Wing wing) {
		this.wing = wing;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public int getCurrentCapacity() {
		return students.size();
	}
	
	public boolean isFull() {
		return getCurrentCapacity() == capacity;
	}
	
}

