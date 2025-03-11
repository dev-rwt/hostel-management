package com.example.hostelmanagement.entity;

import java.util.List;

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
    private Wing wing;

    @ManyToOne
    @JoinColumn(name = "hostel_id")
    private Hostel hostel;
    
    private int capacity;
    

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Student> students;

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

