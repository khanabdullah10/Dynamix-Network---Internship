package com.hotel.reservation.service;

import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.Room.RoomType;
import com.hotel.reservation.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }
    
    public Room updateRoom(Room room) {
        // Validate that the room exists
        Room existingRoom = roomRepository.findById(room.getId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        
        return roomRepository.save(room);
    }
    
    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }
    
    public Optional<Room> findByRoomNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber);
    }
    
    public boolean existsByRoomNumber(String roomNumber) {
        return roomRepository.existsByRoomNumber(roomNumber);
    }
    
    public List<Room> findAll() {
        return roomRepository.findAll();
    }
    
    public List<Room> findByRoomType(RoomType roomType) {
        return roomRepository.findByRoomType(roomType);
    }
    
    public List<Room> findAvailableRooms() {
        return roomRepository.findByIsAvailableTrue();
    }
    
    public List<Room> findAvailableRoomsByType(RoomType roomType) {
        return roomRepository.findAvailableRoomsByType(roomType);
    }
    
    public List<Room> findAvailableRoomsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return roomRepository.findAvailableRoomsByPriceRange(minPrice, maxPrice);
    }
    
    public List<Room> findAvailableRoomsForDateRange(LocalDate checkInDate, LocalDate checkOutDate) {
        return roomRepository.findAvailableRoomsForDateRange(checkInDate, checkOutDate);
    }
    
    public List<Room> findAvailableRoomsByTypeAndDateRange(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        return roomRepository.findAvailableRoomsByTypeAndDateRange(roomType, checkInDate, checkOutDate);
    }
    
    public List<Room> findRoomsWithFilters(RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice, 
                                         Boolean hasWifi, Boolean hasAc, Boolean hasTv) {
        return roomRepository.findRoomsWithFilters(roomType, minPrice, maxPrice, hasWifi, hasAc, hasTv);
    }
    
    public void deleteById(Long id) {
        // Check if room has active reservations before deleting
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        
        // Check for active reservations
        if (!room.getReservations().isEmpty()) {
            boolean hasActiveReservations = room.getReservations().stream()
                    .anyMatch(reservation -> reservation.getStatus() == 
                            com.hotel.reservation.entity.Reservation.ReservationStatus.CONFIRMED ||
                            reservation.getStatus() == 
                            com.hotel.reservation.entity.Reservation.ReservationStatus.PENDING);
            
            if (hasActiveReservations) {
                throw new RuntimeException("Cannot delete room with active reservations");
            }
        }
        
        roomRepository.deleteById(id);
    }
    
    public List<Room> searchRooms(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        
        try {
            // Try to parse as room number
            return List.of(roomRepository.findByRoomNumber(searchTerm).orElse(null))
                    .stream()
                    .filter(room -> room != null)
                    .toList();
        } catch (Exception e) {
            // If not a room number, search by description
            return roomRepository.findAll().stream()
                    .filter(room -> room.getDescription().toLowerCase().contains(searchTerm.toLowerCase()))
                    .toList();
        }
    }
    
    public Room toggleAvailability(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setIsAvailable(!room.getIsAvailable());
        return roomRepository.save(room);
    }
}
