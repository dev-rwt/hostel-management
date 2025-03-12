package com.example.hostelmanagement.controller;

import com.example.hostelmanagement.entity.Hostel;
import com.example.hostelmanagement.entity.Room;
import com.example.hostelmanagement.entity.Wing;
import com.example.hostelmanagement.service.HostelService;
import com.example.hostelmanagement.service.WingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/hostels")
public class HostelController {

    @Autowired
    private HostelService hostelService;
    
    @Autowired
    private WingService wingService;
    
    @GetMapping("")
    public String showHostels(Model model) {
        List<Hostel> hostels = hostelService.getAllHostels();
        model.addAttribute("hostels", hostels);
        return "hostels"; 
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Hostel>> getAllHostels() {
		List<Hostel> hostels = hostelService.getAllHostels();
		return ResponseEntity.ok(hostels);
	}

    @GetMapping("/{id}")
    public String showHostelDetails(@PathVariable Long id, Model model) {
        Hostel hostel = hostelService.getHostelById(id);
        if (hostel == null) {
            return "redirect:/hostels?error=notfound";
        }
        List<Wing> wings = wingService.getWingsByHostel(hostel.getName());
        model.addAttribute("hostel", hostel);
        model.addAttribute("wings", wings);
        return "hostel_details";
    }
    
    @GetMapping("/{id}/wings")
    public ResponseEntity<List<Wing>> getWingsByHostel(@PathVariable Long id) {
		List<Wing> wings = wingService.getWingsByHostelId(id);
		return ResponseEntity.ok(wings);
	}
    
    
    @GetMapping("/{id}/{wing_id}")
    public String showWingDetails(@PathVariable Long id, @PathVariable Long wing_id, Model model) {
		Hostel hostel = hostelService.getHostelById(id);
		if (hostel == null) {
			return "redirect:/hostels?error=notfound";
		}
		List<Wing> wings = wingService.getWingsByHostel(hostel.getName());
		List<Room> rooms = new ArrayList<>();
		for (Wing wing : wings) {
			rooms.addAll(wing.getRooms());
		}
		model.addAttribute("hostel", hostel);
		model.addAttribute("wings", wings);
		model.addAttribute("rooms", rooms);
		return "wing_details";
	}
    
    @GetMapping("/{id}/{wing_id}/rooms")
	public ResponseEntity<List<Room>> getRoomsByWing(@PathVariable Long id, @PathVariable Long wing_id) {
    	Wing wing = wingService.getWingById(wing_id);
		List<Room> rooms = wing.getRooms();
		return ResponseEntity.ok(rooms);
	}
    
 // Add Hostel Page
    @GetMapping("/add")
    public String showAddHostelForm() {
        return "add-hostel";
    }

    // Add Wing Page
    @GetMapping("/add/wings")
    public String showAddWingForm(Model model) {
        model.addAttribute("hostels", hostelService.getAllHostels());
        return "add-wing";
    }

    // Add Room Page
    @GetMapping("/add/rooms")
    public String showAddRoomForm(Model model) {
        model.addAttribute("hostels", hostelService.getAllHostels());
        model.addAttribute("wings", wingService.getAllWings());
        return "add-room";
    }

    // Add Hostel API with Flash Message
    @PostMapping("/add")
    public String addHostel(@RequestParam String name, RedirectAttributes redirectAttributes) {
        try {
            hostelService.addHostel(name);
            redirectAttributes.addFlashAttribute("success", "Hostel added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding hostel: " + e.getMessage());
        }
        return "redirect:/hostels/add";
    }

    // Add Wing API with Flash Message
    @PostMapping("/add/wings")
    public String addWing(@RequestParam String hostelName, @RequestParam String wingName, RedirectAttributes redirectAttributes) {
        try {
            hostelService.addWingToHostel(wingName, hostelName);
            redirectAttributes.addFlashAttribute("success", "Wing added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding wing: " + e.getMessage());
        }
        return "redirect:/hostels/add/wings";
    }

    // Add Room API with Flash Message
    @PostMapping("/add/rooms")
    public String addRoom(@RequestParam String hostelName, @RequestParam String wingName,
                          @RequestParam String roomNumber, @RequestParam int floor, 
                          @RequestParam int capacity, RedirectAttributes redirectAttributes) {
        try {
            hostelService.addRoomToWing(hostelName, wingName, roomNumber, floor, capacity);
            redirectAttributes.addFlashAttribute("success", "Room added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding room: " + e.getMessage());
        }
        return "redirect:/hostels/add/rooms";
    }
//    @PatchMapping("/{id}")
//    public ResponseEntity<Hostel> updateHostel(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
//        Hostel updatedHostel = hostelService.updateHostel(id, updates);
//        return ResponseEntity.ok(updatedHostel);
//    }
//    
//    @GetMapping("/{id}/rooms")
//    public ResponseEntity<?> showHostelRooms(@PathVariable Long id) {
//        List<Room> rooms = hostelService.getRoomsByHostelId(id);
//        if (rooms.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(rooms);
//    }
//    
//    @GetMapping("{id}/available-rooms")
//    public ResponseEntity<List<Room>> getAvailableRooms(@PathVariable Long id) {
//        List<Room> rooms = roomService.getRoomsByHostel(id);
//        return ResponseEntity.ok(rooms);
//    }
    
    
    
}
