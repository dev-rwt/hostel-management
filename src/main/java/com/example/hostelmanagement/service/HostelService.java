package com.example.hostelmanagement.service;

import com.example.hostelmanagement.entity.Hostel;
import com.example.hostelmanagement.entity.Room;
import com.example.hostelmanagement.entity.Wing;
import com.example.hostelmanagement.repository.HostelRepository;
import com.example.hostelmanagement.repository.RoomRepository;
import com.example.hostelmanagement.repository.WingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HostelService {

    @Autowired
    private HostelRepository hostelRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private WingRepository wingRepository;


    public List<Hostel> getAllHostels() {
        return hostelRepository.findAll();
    }

    public Hostel getHostelById(Long id) {
        return hostelRepository.findById(id).orElse(null);
    }
    
    // Add Hostel
    public Hostel addHostel(String name) {
        Hostel hostel = new Hostel();
        hostel.setName(name);
        return hostelRepository.save(hostel);
    }

    // Add Wing to a Hostel
    public Wing addWingToHostel(String wingName, String hostelName) {
        Hostel hostel = hostelRepository.findByName(hostelName)
                .orElseThrow(() -> new RuntimeException("Hostel not found"));
        
        Wing wing = new Wing();
        wing.setName(wingName);
        wing.setHostel(hostel);

        return wingRepository.save(wing);
    }

    // Add Room to a Wing in a Hostel
    public Room addRoomToWing(String hostelName, String wingName,String roomNumber, int floor, int capacity ) {
        Hostel hostel = hostelRepository.findByName(hostelName)
                .orElseThrow(() -> new RuntimeException("Hostel not found"));
        
        Wing wing = wingRepository.findByNameAndHostel(wingName, hostel)
                .orElseThrow(() -> new RuntimeException("Wing not found"));

        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setFloor(floor);
        room.setCapacity(capacity);
        room.setWing(wing);
        room.setHostel(hostel);

        return roomRepository.save(room);
    }

//	public Hostel updateHostel(Long id, Map<String, Object> updates) {
//		hostelRepository.findById(id).ifPresent(hostel -> {
//			if (updates.containsKey("name")) {
//				hostel.setName((String) updates.get("name"));
//			}
//			if (updates.containsKey("wings")) {
//				hostel.setWing((List<Wing>) updates.get("wings"));
//			}
//			hostelRepository.save(hostel);
//		});
//	}

  
}
    
