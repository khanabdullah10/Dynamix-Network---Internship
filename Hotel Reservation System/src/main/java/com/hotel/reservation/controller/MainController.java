package com.hotel.reservation.controller;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.User;
import com.hotel.reservation.service.ReservationService;
import com.hotel.reservation.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class MainController {
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private ReservationService reservationService;
    
    @GetMapping("/")
    public String home(Model model) {
        List<Room> featuredRooms = roomService.findAvailableRooms().stream()
                .limit(6)
                .toList();
        
        model.addAttribute("featuredRooms", featuredRooms);
        return "home";
    }
    
    @GetMapping("/rooms")
    public String rooms(@RequestParam(required = false) String roomType,
                       @RequestParam(required = false) String checkInDate,
                       @RequestParam(required = false) String checkOutDate,
                       @RequestParam(required = false) String minPrice,
                       @RequestParam(required = false) String maxPrice,
                       Model model) {
        
        List<Room> rooms;
        
        if (checkInDate != null && !checkInDate.isEmpty() && 
            checkOutDate != null && !checkOutDate.isEmpty()) {
            
            LocalDate checkIn = LocalDate.parse(checkInDate);
            LocalDate checkOut = LocalDate.parse(checkOutDate);
            
            if (roomType != null && !roomType.isEmpty()) {
                rooms = roomService.findAvailableRoomsByTypeAndDateRange(
                    Room.RoomType.valueOf(roomType), checkIn, checkOut);
            } else {
                rooms = roomService.findAvailableRoomsForDateRange(checkIn, checkOut);
            }
        } else {
            rooms = roomService.findAvailableRooms();
        }
        
        model.addAttribute("rooms", rooms);
        model.addAttribute("roomTypes", Room.RoomType.values());
        model.addAttribute("selectedRoomType", roomType);
        model.addAttribute("checkInDate", checkInDate);
        model.addAttribute("checkOutDate", checkOutDate);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        
        return "rooms";
    }
    
    @GetMapping("/rooms/{id}")
    public String roomDetails(@PathVariable Long id, Model model) {
        Room room = roomService.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        
        model.addAttribute("room", room);
        return "room-details";
    }
    
    @GetMapping("/rooms/{id}/book")
    public String bookRoomForm(@PathVariable Long id, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login?book=true";
        }
        
        Room room = roomService.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        
        Reservation reservation = new Reservation();
        reservation.setRoom(room);
        reservation.setCheckInDate(LocalDate.now().plusDays(1));
        reservation.setCheckOutDate(LocalDate.now().plusDays(2));
        reservation.setNumberOfGuests(1);
        
        model.addAttribute("reservation", reservation);
        model.addAttribute("room", room);
        
        return "book-room";
    }
    
    @PostMapping("/rooms/{id}/book")
    public String bookRoom(@PathVariable Long id, @Valid @ModelAttribute Reservation reservation, 
                          BindingResult result, Model model, Authentication authentication) {
        
        if (result.hasErrors()) {
            Room room = roomService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Room not found"));
            model.addAttribute("room", room);
            return "book-room";
        }
        
        User user = (User) authentication.getPrincipal();
        Room room = roomService.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        
        try {
            Reservation newReservation = reservationService.createReservation(
                user, room, reservation.getCheckInDate(), reservation.getCheckOutDate(),
                reservation.getNumberOfGuests(), reservation.getSpecialRequests()
            );
            
            return "redirect:/reservations/my?bookingSuccess=true&confirmation=" + 
                   newReservation.getConfirmationNumber();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("room", room);
            return "book-room";
        }
    }
    
    @GetMapping("/reservations/my")
    public String myReservations(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        
        User user = (User) authentication.getPrincipal();
        List<Reservation> reservations = reservationService.findByUserOrderByCreatedAtDesc(user);
        
        model.addAttribute("reservations", reservations);
        return "my-reservations";
    }
    
    @PostMapping("/reservations/cancel/{confirmationNumber}")
    public String cancelReservation(@PathVariable String confirmationNumber, Authentication authentication) {
        try {
            reservationService.cancelReservationByConfirmationNumber(confirmationNumber);
            return "redirect:/reservations/my?cancelSuccess=true";
        } catch (Exception e) {
            return "redirect:/reservations/my?cancelError=true";
        }
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        
        User user = (User) authentication.getPrincipal();
        
        if (user.getRole() == User.Role.ADMIN) {
            // Admin dashboard
            List<Reservation> recentReservations = reservationService.findAll().stream()
                    .limit(10)
                    .toList();
            
            Long totalRooms = (long) roomService.findAll().size();
            Long availableRooms = (long) roomService.findAvailableRooms().size();
            Long totalReservations = (long) reservationService.findAll().size();
            
            model.addAttribute("recentReservations", recentReservations);
            model.addAttribute("totalRooms", totalRooms);
            model.addAttribute("availableRooms", availableRooms);
            model.addAttribute("totalReservations", totalReservations);
            
            return "admin/dashboard";
        } else {
            // Guest dashboard
            List<Reservation> userReservations = reservationService.findByUserOrderByCreatedAtDesc(user)
                    .stream()
                    .limit(5)
                    .toList();
            
            model.addAttribute("reservations", userReservations);
            return "guest/dashboard";
        }
    }
    
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q, Model model) {
        if (q == null || q.isEmpty()) {
            return "redirect:/rooms";
        }
        List<Room> rooms = roomService.searchRooms(q);
        model.addAttribute("rooms", rooms);
        model.addAttribute("searchTerm", q);
        return "search-results";
    }
}
