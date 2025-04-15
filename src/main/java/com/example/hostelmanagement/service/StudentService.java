package com.example.hostelmanagement.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.hostelmanagement.entity.AppUser;
import com.example.hostelmanagement.entity.Room;
import com.example.hostelmanagement.entity.Student;
import com.example.hostelmanagement.repository.UserRepository;
import com.example.hostelmanagement.repository.RoomRepository;
import com.example.hostelmanagement.repository.StudentRepository;


@Service
public class StudentService {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public boolean deleteStudent(String name, String rollNumber) {
        Optional<Student> student = studentRepository.findByNameAndRollNumber(name, rollNumber);
        if (student.isPresent()) {
            studentRepository.delete(student.get());
            return true;
        }
        return false;
    }

    public List<Student> getUnallocatedStudents() {
        return studentRepository.findUnallocatedStudents();    
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue()); // Convert number to string
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    
    public void saveStudentsFromExcel(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            List<Student> students = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                String name = getCellValueAsString(row.getCell(0));
                String rollNumber = getCellValueAsString(row.getCell(1));
                String gender = getCellValueAsString(row.getCell(2));
                String email = getCellValueAsString(row.getCell(3));
                String phoneNo = getCellValueAsString(row.getCell(4));
                String hostelName = getCellValueAsString(row.getCell(5));
                String wingName = getCellValueAsString(row.getCell(6));
                String roomNo = getCellValueAsString(row.getCell(7));
                
                
                Room room = null;
                if (!roomNo.isEmpty()) {
                	room = roomRepository.findByRoomNumberAndWingAndHostel(roomNo, wingName, hostelName)
                	        .orElseThrow(() -> new RuntimeException("Room not found in the given Wing and Hostel"));

                }
                
                
                Student student = new Student();
                student.setName(name);
                student.setRollNumber(rollNumber);
                student.setGender(gender);
                student.setEmail(email);
                student.setPhoneNo(phoneNo);
                student.setRoom(room); // Set the room entity
                

                students.add(student);
            }

            workbook.close();
            studentRepository.saveAll(students);

        } catch (Exception e) {
            throw new RuntimeException("Failed to process the Excel file: " + e.getMessage());
        }
    }

	public List<Student> findAll() {
		return studentRepository.findAll();
	        
	}
	
	public Student createStudent(Student student) {
        Optional<AppUser> user = userRepository.findByEmail(student.getEmail());
        
        if (user == null) {
            throw new RuntimeException("No user found with email: " + student.getEmail());
        }

        student.setUser(user.get()); // Link Student to User
        return studentRepository.save(student);
    }

	public boolean deleteStudentById(Long id) {
		Optional<Student> student = studentRepository.findById(id);
		if (student.isPresent()) {
			studentRepository.delete(student.get());
			return true;
		}
		return false;
	}



	public Student findById(Long id) {
		return studentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
	}

	public Student getStudentByEmail(String email) {
		return studentRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Student not found with email: " + email));
	}
}
