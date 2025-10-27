package com.hotel.reservation.service;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Reservation.ReservationStatus;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.User;
import com.hotel.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
    
    public Reservation updateReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
    
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }
    
    public Optional<Reservation> findByConfirmationNumber(String confirmationNumber) {
        return reservationRepository.findByConfirmationNumber(confirmationNumber);
    }
    
    public List<Reservation> findByUser(User user) {
        return reservationRepository.findByUser(user);
    }
    
    public List<Reservation> findByUserOrderByCreatedAtDesc(User user) {
        return reservationRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public List<Reservation> findByRoom(Room room) {
        return reservationRepository.findByRoom(room);
    }
    
    public List<Reservation> findByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }
    
    public List<Reservation> findUpcomingReservations(LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findUpcomingReservations(startDate, endDate);
    }
    
    public List<Reservation> findActiveReservationsOnDate(LocalDate date) {
        return reservationRepository.findActiveReservationsOnDate(date);
    }
    
    public Long countActiveReservationsOnDate(LocalDate date) {
        return reservationRepository.countActiveReservationsOnDate(date);
    }
    
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
    
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
    
    public Reservation createReservation(User user, Room room, LocalDate checkInDate, 
                                        LocalDate checkOutDate, Integer numberOfGuests, String specialRequests) {
        
        // Validate dates
        if (checkInDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }
        if (checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        
        // Check for conflicts
        List<Reservation> conflicts = reservationRepository.findConflictingReservationsForRoom(room, checkInDate, checkOutDate);
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Room is not available for the selected dates");
        }
        
        // Validate number of guests
        if (numberOfGuests > room.getMaxOccupancy()) {
            throw new IllegalArgumentException("Number of guests exceeds room capacity");
        }
        
        Reservation reservation = new Reservation(user, room, checkInDate, checkOutDate, numberOfGuests);
        reservation.setSpecialRequests(specialRequests);
        
        return saveReservation(reservation);
    }
    
    public Reservation confirmReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return saveReservation(reservation);
    }
    
    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        
        if (!reservation.canBeCancelled()) {
            throw new IllegalArgumentException("Reservation cannot be cancelled");
        }
        
        reservation.setStatus(ReservationStatus.CANCELLED);
        return saveReservation(reservation);
    }
    
    public Reservation cancelReservationByConfirmationNumber(String confirmationNumber) {
        Reservation reservation = reservationRepository.findByConfirmationNumber(confirmationNumber)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        
        if (!reservation.canBeCancelled()) {
            throw new IllegalArgumentException("Reservation cannot be cancelled");
        }
        
        reservation.setStatus(ReservationStatus.CANCELLED);
        return saveReservation(reservation);
    }
    
    public boolean isRoomAvailable(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Reservation> conflicts = reservationRepository.findConflictingReservationsForRoom(room, checkInDate, checkOutDate);
        return conflicts.isEmpty();
    }
    
    public Long countReservationsByMonth(int year, int month) {
        return reservationRepository.countReservationsByMonth(year, month);
    }
    
    public java.math.BigDecimal getTotalRevenueByMonth(int year, int month) {
        return reservationRepository.getTotalRevenueByMonth(year, month);
    }
    
    public List<Reservation> searchReservations(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        
        // Try to find by confirmation number first
        Optional<Reservation> byConfirmation = reservationRepository.findByConfirmationNumber(searchTerm);
        if (byConfirmation.isPresent()) {
            return List.of(byConfirmation.get());
        }
        
        // Search by user name
        return reservationRepository.findAll().stream()
                .filter(reservation -> 
                    reservation.getUser().getFirstName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    reservation.getUser().getLastName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    reservation.getUser().getEmail().toLowerCase().contains(searchTerm.toLowerCase())
                )
                .toList();
    }
}
