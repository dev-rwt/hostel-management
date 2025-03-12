package com.example.hostelmanagement.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;


@Data
@Entity
public class Wing {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "hostel_id")
	@JsonIgnore
	private Hostel hostel;
	
	@OneToMany(mappedBy = "wing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Room> rooms;
	
	public Long getId() {
		return id;
	}


	public Hostel getHostel() {
		return hostel;
	}

	public void setHostel(Hostel hostel) {
		this.hostel = hostel;
	}

	
	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getOccupiedCapacity() {
		return rooms.stream()
				.mapToInt(Room::getCurrentCapacity)
				.sum();
	}
	
	public int getCapacity() {
		return rooms.stream()
				.mapToInt(Room::getCapacity)
				.sum();
	}
}
