package com.hotel.reservation.repository;

import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.Room.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    Optional<Room> findByRoomNumber(String roomNumber);
    
    boolean existsByRoomNumber(String roomNumber);
    
    List<Room> findByRoomType(RoomType roomType);
    
    List<Room> findByIsAvailableTrue();
    
    List<Room> findByPricePerNightBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("SELECT r FROM Room r WHERE r.isAvailable = true AND r.roomType = :roomType")
    List<Room> findAvailableRoomsByType(@Param("roomType") RoomType roomType);
    
    @Query("SELECT r FROM Room r WHERE r.isAvailable = true AND r.pricePerNight BETWEEN :minPrice AND :maxPrice")
    List<Room> findAvailableRoomsByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT DISTINCT r FROM Room r LEFT JOIN r.reservations res " +
           "WHERE r.isAvailable = true " +
           "AND (res IS NULL OR " +
           "NOT (res.checkInDate <= :checkOutDate AND res.checkOutDate >= :checkInDate " +
           "AND res.status IN ('CONFIRMED', 'PENDING')))")
    List<Room> findAvailableRoomsForDateRange(@Param("checkInDate") LocalDate checkInDate, 
                                              @Param("checkOutDate") LocalDate checkOutDate);
    
    @Query("SELECT DISTINCT r FROM Room r LEFT JOIN r.reservations res " +
           "WHERE r.isAvailable = true " +
           "AND r.roomType = :roomType " +
           "AND (res IS NULL OR " +
           "NOT (res.checkInDate <= :checkOutDate AND res.checkOutDate >= :checkInDate " +
           "AND res.status IN ('CONFIRMED', 'PENDING')))")
    List<Room> findAvailableRoomsByTypeAndDateRange(@Param("roomType") RoomType roomType,
                                                    @Param("checkInDate") LocalDate checkInDate, 
                                                    @Param("checkOutDate") LocalDate checkOutDate);
    
    @Query("SELECT r FROM Room r WHERE r.isAvailable = true " +
           "AND (:roomType IS NULL OR r.roomType = :roomType) " +
           "AND (:minPrice IS NULL OR r.pricePerNight >= :minPrice) " +
           "AND (:maxPrice IS NULL OR r.pricePerNight <= :maxPrice) " +
           "AND (:hasWifi IS NULL OR r.hasWifi = :hasWifi) " +
           "AND (:hasAc IS NULL OR r.hasAc = :hasAc) " +
           "AND (:hasTv IS NULL OR r.hasTv = :hasTv)")
    List<Room> findRoomsWithFilters(@Param("roomType") RoomType roomType,
                                   @Param("minPrice") BigDecimal minPrice,
                                   @Param("maxPrice") BigDecimal maxPrice,
                                   @Param("hasWifi") Boolean hasWifi,
                                   @Param("hasAc") Boolean hasAc,
                                   @Param("hasTv") Boolean hasTv);
}
