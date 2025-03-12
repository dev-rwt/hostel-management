package com.example.hostelmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Data
@Entity

@Table(name = "hostels")
public class Hostel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 

    private String name; 
    
    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Wing> wings; 
    

    

	public Long getId() {
		return id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Wing> getWing() {
		return wings;
	}
	
	public void setWing(List<Wing> wings) {
		this.wings = wings;
	}
	
	public int getOccupiedCapacity() {
		return wings.stream()
				.flatMap(wing -> wing.getRooms().stream())
				.mapToInt(Room::getCurrentCapacity)
				.sum();
	}

	public int getCapacity() {
        return wings.stream()
                .flatMap(wing -> wing.getRooms().stream())
                .mapToInt(Room::getCapacity)
                .sum();
    }
	
}
