package com.hotel.reservation.repository;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Reservation.ReservationStatus;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    Optional<Reservation> findByConfirmationNumber(String confirmationNumber);
    
    List<Reservation> findByUser(User user);
    
    List<Reservation> findByRoom(Room room);
    
    List<Reservation> findByStatus(ReservationStatus status);
    
    List<Reservation> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Reservation> findByCheckOutDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT r FROM Reservation r WHERE r.user = :user ORDER BY r.createdAt DESC")
    List<Reservation> findByUserOrderByCreatedAtDesc(@Param("user") User user);
    
    @Query("SELECT r FROM Reservation r WHERE r.status IN ('PENDING', 'CONFIRMED') " +
           "AND r.checkInDate BETWEEN :startDate AND :endDate")
    List<Reservation> findUpcomingReservations(@Param("startDate") LocalDate startDate, 
                                              @Param("endDate") LocalDate endDate);
    
    @Query("SELECT r FROM Reservation r WHERE r.status = 'CONFIRMED' " +
           "AND r.checkInDate <= :date AND r.checkOutDate >= :date")
    List<Reservation> findActiveReservationsOnDate(@Param("date") LocalDate date);
    
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.status = 'CONFIRMED' " +
           "AND r.checkInDate <= :date AND r.checkOutDate >= :date")
    Long countActiveReservationsOnDate(@Param("date") LocalDate date);
    
    @Query("SELECT r FROM Reservation r WHERE r.status IN ('CONFIRMED', 'PENDING') " +
           "AND ((r.checkInDate <= :checkInDate AND r.checkOutDate > :checkInDate) " +
           "OR (r.checkInDate < :checkOutDate AND r.checkOutDate >= :checkOutDate) " +
           "OR (r.checkInDate >= :checkInDate AND r.checkOutDate <= :checkOutDate))")
    List<Reservation> findConflictingReservations(@Param("checkInDate") LocalDate checkInDate,
                                                  @Param("checkOutDate") LocalDate checkOutDate);
    
    @Query("SELECT r FROM Reservation r WHERE r.status IN ('CONFIRMED', 'PENDING') " +
           "AND r.room = :room " +
           "AND ((r.checkInDate <= :checkInDate AND r.checkOutDate > :checkInDate) " +
           "OR (r.checkInDate < :checkOutDate AND r.checkOutDate >= :checkOutDate) " +
           "OR (r.checkInDate >= :checkInDate AND r.checkOutDate <= :checkOutDate))")
    List<Reservation> findConflictingReservationsForRoom(@Param("room") Room room,
                                                          @Param("checkInDate") LocalDate checkInDate,
                                                          @Param("checkOutDate") LocalDate checkOutDate);
    
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.status = 'CONFIRMED' " +
           "AND YEAR(r.checkInDate) = :year AND MONTH(r.checkInDate) = :month")
    Long countReservationsByMonth(@Param("year") int year, @Param("month") int month);
    
    @Query("SELECT SUM(r.totalAmount) FROM Reservation r WHERE r.status = 'CONFIRMED' " +
           "AND YEAR(r.checkInDate) = :year AND MONTH(r.checkInDate) = :month")
    java.math.BigDecimal getTotalRevenueByMonth(@Param("year") int year, @Param("month") int month);
}
