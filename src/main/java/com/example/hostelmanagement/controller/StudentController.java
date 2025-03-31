package com.example.hostelmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.hostelmanagement.entity.Student;
import com.example.hostelmanagement.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {
	@Autowired
	private StudentService studentService;
	
	@GetMapping("/unallocated")
	public ResponseEntity<List<Student>> getUnallocatedStudents() {
	    List<Student> students = studentService.getUnallocatedStudents();
	    return ResponseEntity.ok(students);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Student>> getAllStudents() {
	    List<Student> students = studentService.findAll();
	    return ResponseEntity.ok(students);
	}
	
	@GetMapping("/all")
    public String getAllStudents(Model model) {
        List<Student> students = studentService.findAll();
        model.addAttribute("students", students);
        return "students";
    }
	

	    @PostMapping("/upload")
	    public String uploadExcelFile(@RequestParam("file") MultipartFile file, Model model) {
	        if (file.isEmpty()) {
	            model.addAttribute("message", "Please select a file to upload.");
	            return "upload";
	        }

	        try {
	            studentService.saveStudentsFromExcel(file);
	            model.addAttribute("message", "File uploaded successfully!");
	        } catch (Exception e) {
	            model.addAttribute("message", "Error uploading file: " + e.getMessage());
	        }

	        return "upload";
	    }
	    
	    @GetMapping("/delete-student")
	    public String showDeleteStudentPage() {
	        return "delete_students"; // Return the delete student HTML page
	    }

	    @PostMapping("/delete-student")
	    public String deleteStudent(@RequestParam String name, @RequestParam String rollNumber, Model model) {
	        boolean isDeleted = studentService.deleteStudent(name, rollNumber);
	        if (isDeleted) {
	            model.addAttribute("message", "Student deleted successfully!");
	        } else {
	            model.addAttribute("error", "Student not found!");
	        }
	        return "delete_students";
	    }

}
