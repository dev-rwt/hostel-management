package com.example.hostelmanagement.repository;

import com.example.hostelmanagement.entity.Hostel;
import com.example.hostelmanagement.entity.Room;
import com.example.hostelmanagement.entity.Student;
import com.example.hostelmanagement.entity.Wing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHostelId(Long hostelId);
    
    @Query("SELECT r FROM Room r WHERE r.hostel = :hostel AND r.roomNumber = :roomNo")
    Optional<Room> findByHostelAndRoomNumber(@Param("hostel") Hostel hostel, @Param("roomNo") String roomNo);
    
    boolean existsByHostelAndRoomNumber(Hostel hostel, String roomNumber);

	Optional<Room> findByroomNumber(String roomNo);
	
	@Query("SELECT r FROM Room r JOIN r.wing w JOIN w.hostel h WHERE r.roomNumber = :roomNumber AND w.name = :wingName AND h.name = :hostelName")
	Optional<Room> findByRoomNumberAndWingAndHostel(@Param("roomNumber") String roomNumber, @Param("wingName") String wingName, @Param("hostelName") String hostelName);
	
	
}
