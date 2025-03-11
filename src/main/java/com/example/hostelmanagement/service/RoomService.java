package com.example.hostelmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hostelmanagement.entity.Hostel;
import com.example.hostelmanagement.entity.Room;
import com.example.hostelmanagement.repository.HostelRepository;
import com.example.hostelmanagement.repository.RoomRepository;

@Service
public class RoomService {
	
	@Autowired
	private final RoomRepository roomRepository;
	
	@Autowired
	private HostelRepository hostelRepository;

	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	public Room saveRoom(Room room) {
		return roomRepository.save(room);
	}

	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	public Room getRoomById(Long roomId) {
		return roomRepository.findById(roomId).orElse(null);
	}

	public void deleteRoom(Long roomId) {
		roomRepository.deleteById(roomId);
	}

	public Room updateRoom(Room room) {
		return roomRepository.save(room);
	}

	public List<Room> getRoomsByHostel(Long hostelId) {
        return roomRepository.findByHostelId(hostelId);
    }

	 public void addRoomsToHostel(Long hostelId, int fromRoom, int toRoom) {
	        Hostel hostel = hostelRepository.findById(hostelId)
	                .orElseThrow(() -> new RuntimeException("Hostel not found"));

	        for (int roomNumber = fromRoom; roomNumber <= toRoom; roomNumber++) {
	        	if (!roomRepository.existsByHostelAndRoomNumber(hostel, String.valueOf(roomNumber))) {
	            Room room = new Room();
	            room.setRoomNumber(String.valueOf(roomNumber));
	            room.setHostel(hostel);  
	            roomRepository.save(room);
	        	}
	        }
	    }

	

}
