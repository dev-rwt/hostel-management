package com.example.hostelmanagement.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hostelmanagement.entity.Hostel;
import com.example.hostelmanagement.entity.Room;
import com.example.hostelmanagement.entity.Student;
import com.example.hostelmanagement.repository.HostelRepository;
import com.example.hostelmanagement.repository.RoomRepository;
import com.example.hostelmanagement.repository.StudentRepository;

@Service
public class AllocationService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HostelRepository hostelRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    

    public List<Hostel> getAllHostels() {
        Set<Hostel> set = new HashSet<>(hostelRepository.findAllUnique());  
        List<Hostel> uniqueList = new ArrayList<>(set);
        return uniqueList;
    }

    public boolean allocateRoom(String studentName, Long hostelId, Long roomId) {
        Student student = studentRepository.findByName(studentName);
        Room room = roomRepository.findById(roomId).orElse(null);

        if (student != null && room != null && room.getHostel().getId().equals(hostelId)) {
            student.setRoom(room);
            studentRepository.save(student);
            return true;
        }
        return false;
    }
}
