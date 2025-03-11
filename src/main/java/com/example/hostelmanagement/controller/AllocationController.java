package com.example.hostelmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.hostelmanagement.service.AllocationService;

@Controller
@RequestMapping("/allocate")
public class AllocationController {

    @Autowired
    private AllocationService allocationService;

    @GetMapping
    public String showAllocationForm(Model model) {
        model.addAttribute("students", allocationService.getAllStudents());
        model.addAttribute("hostels", allocationService.getAllHostels());
        return "room_allot"; 
    }

    @PostMapping
    public String allocateRoom(@RequestParam String studentName, @RequestParam Long hostelId, @RequestParam Long roomId) {
        boolean success = allocationService.allocateRoom(studentName, hostelId, roomId);
        return success ? "redirect:/allocate?success" : "redirect:/allocate?error";
    }
}

