package com.hotel.reservation.controller;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.User;
import com.hotel.reservation.service.ReservationService;
import com.hotel.reservation.service.RoomService;
import com.hotel.reservation.service.UserService;
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
@RequestMapping("/guest")
public class GuestController {
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/rooms")
    public String viewRooms(@RequestParam(required = false) String roomType,
                           @RequestParam(required = false) String checkInDate,
                           @RequestParam(required = false) String checkOutDate,
                           @RequestParam(required = false) String minPrice,
                           @RequestParam(required = false) String maxPrice,
                           Authentication authentication,
                           Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        
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
        
        return "guest/rooms";
    }
    
    @GetMapping("/rooms/{id}")
    public String viewRoomDetails(@PathVariable Long id, Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        Room room = roomService.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        
        model.addAttribute("room", room);
        return "guest/room-details";
    }
    
    @GetMapping("/reservations")
    public String myReservations(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        User user = (User) authentication.getPrincipal();
        List<Reservation> reservations = reservationService.findByUserOrderByCreatedAtDesc(user);
        
        model.addAttribute("reservations", reservations);
        return "guest/my-reservations";
    }
    
    @PostMapping("/reservations/cancel/{id}")
    public String cancelReservation(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        try {
            reservationService.cancelReservation(id);
            return "redirect:/guest/reservations?cancelSuccess=true";
        } catch (Exception e) {
            return "redirect:/guest/reservations?cancelError=true";
        }
    }
    
    @GetMapping("/profile")
    public String viewProfile(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "guest/profile";
    }
    
    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute User user, BindingResult result, 
                               Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        if (result.hasErrors()) {
            return "guest/profile";
        }
        
        User currentUser = (User) authentication.getPrincipal();
        
        // Get the full user object from database to avoid data loss
        User existingUser = userService.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update only the fields that should be updated
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        
        userService.updateUser(existingUser);
        
        return "redirect:/guest/profile?updateSuccess=true";
    }
}
