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
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private UserService userService;
    
    private boolean isAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        User user = (User) authentication.getPrincipal();
        return user.getRole() == User.Role.ADMIN;
    }
    
    // Room Management
    @GetMapping("/rooms")
    public String manageRooms(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Authentication authentication,
                             Model model) {
        if (!isAdmin(authentication)) {
            return "redirect:/auth/login";
        }
        List<Room> rooms = roomService.findAll();
        model.addAttribute("rooms", rooms);
        model.addAttribute("roomTypes", Room.RoomType.values());
        return "admin/rooms/manage";
    }
    
    @GetMapping("/rooms/add")
    public String addRoomForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("roomTypes", Room.RoomType.values());
        return "admin/rooms/add";
    }
    
    @PostMapping("/rooms/add")
    public String addRoom(@Valid @ModelAttribute Room room, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roomTypes", Room.RoomType.values());
            return "admin/rooms/add";
        }
        
        if (roomService.existsByRoomNumber(room.getRoomNumber())) {
            result.rejectValue("roomNumber", "error.room", "Room number already exists");
            model.addAttribute("roomTypes", Room.RoomType.values());
            return "admin/rooms/add";
        }
        
        roomService.saveRoom(room);
        return "redirect:/admin/rooms?addSuccess=true";
    }
    
    @GetMapping("/rooms/edit/{id}")
    public String editRoomForm(@PathVariable Long id, Model model) {
        Room room = roomService.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        
        model.addAttribute("room", room);
        model.addAttribute("roomTypes", Room.RoomType.values());
        return "admin/rooms/edit";
    }
    
    @PostMapping("/rooms/edit/{id}")
    public String editRoom(@PathVariable Long id, @Valid @ModelAttribute Room room, 
                          BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roomTypes", Room.RoomType.values());
            return "admin/rooms/edit";
        }
        
        Room existingRoom = roomService.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        
        if (!existingRoom.getRoomNumber().equals(room.getRoomNumber()) && 
            roomService.existsByRoomNumber(room.getRoomNumber())) {
            result.rejectValue("roomNumber", "error.room", "Room number already exists");
            model.addAttribute("roomTypes", Room.RoomType.values());
            return "admin/rooms/edit";
        }
        
        roomService.updateRoom(room);
        return "redirect:/admin/rooms?updateSuccess=true";
    }
    
    @PostMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteById(id);
        return "redirect:/admin/rooms?deleteSuccess=true";
    }
    
    @PostMapping("/rooms/toggle/{id}")
    public String toggleRoomAvailability(@PathVariable Long id) {
        roomService.toggleAvailability(id);
        return "redirect:/admin/rooms?toggleSuccess=true";
    }
    
    // Reservation Management
    @GetMapping("/reservations")
    public String manageReservations(@RequestParam(required = false) String status,
                                   @RequestParam(required = false) String search,
                                   Model model) {
        List<Reservation> reservations;
        
        if (search != null && !search.isEmpty()) {
            reservations = reservationService.searchReservations(search);
        } else if (status != null && !status.isEmpty()) {
            reservations = reservationService.findByStatus(Reservation.ReservationStatus.valueOf(status));
        } else {
            reservations = reservationService.findAll();
        }
        
        model.addAttribute("reservations", reservations);
        model.addAttribute("statuses", Reservation.ReservationStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("searchTerm", search);
        
        return "admin/reservations/manage";
    }
    
    @PostMapping("/reservations/confirm/{id}")
    public String confirmReservation(@PathVariable Long id) {
        reservationService.confirmReservation(id);
        return "redirect:/admin/reservations?confirmSuccess=true";
    }
    
    @PostMapping("/reservations/cancel/{id}")
    public String cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return "redirect:/admin/reservations?cancelSuccess=true";
    }
    
    @GetMapping("/reservations/{id}")
    public String viewReservationDetails(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        
        model.addAttribute("reservation", reservation);
        return "admin/reservations/details";
    }
    
    // User Management
    @GetMapping("/users")
    public String manageUsers(@RequestParam(required = false) String role,
                             @RequestParam(required = false) String search,
                             Model model) {
        List<User> users;
        
        if (search != null && !search.isEmpty()) {
            users = userService.findByNameContaining(search);
        } else if (role != null && !role.isEmpty()) {
            if ("ADMIN".equals(role)) {
                users = userService.findAllAdmins();
            } else {
                users = userService.findAllGuests();
            }
        } else {
            users = userService.findAll();
        }
        
        model.addAttribute("users", users);
        model.addAttribute("roles", User.Role.values());
        model.addAttribute("selectedRole", role);
        model.addAttribute("searchTerm", search);
        
        return "admin/users/manage";
    }
    
    @GetMapping("/users/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", User.Role.values());
        return "admin/users/add";
    }
    
    @PostMapping("/users/add")
    public String addUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", User.Role.values());
            return "admin/users/add";
        }
        
        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
            model.addAttribute("roles", User.Role.values());
            return "admin/users/add";
        }
        
        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email already exists");
            model.addAttribute("roles", User.Role.values());
            return "admin/users/add";
        }
        
        userService.saveUser(user);
        return "redirect:/admin/users?addSuccess=true";
    }
    
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users?deleteSuccess=true";
    }
    
    // Reports
    @GetMapping("/reports")
    public String reports(@RequestParam(required = false) String reportType,
                         @RequestParam(required = false) String month,
                         @RequestParam(required = false) String year,
                         Model model) {
        
        if (reportType != null && !reportType.isEmpty()) {
            switch (reportType) {
                case "occupancy":
                    // Generate occupancy report
                    LocalDate currentDate = LocalDate.now();
                    Long occupancyCount = reservationService.countActiveReservationsOnDate(currentDate);
                    model.addAttribute("occupancyCount", occupancyCount);
                    model.addAttribute("reportDate", currentDate);
                    break;
                    
                case "revenue":
                    if (month != null && year != null) {
                        int monthInt = Integer.parseInt(month);
                        int yearInt = Integer.parseInt(year);
                        java.math.BigDecimal revenue = reservationService.getTotalRevenueByMonth(yearInt, monthInt);
                        model.addAttribute("revenue", revenue);
                        model.addAttribute("reportMonth", monthInt);
                        model.addAttribute("reportYear", yearInt);
                    }
                    break;
                    
                case "upcoming":
                    LocalDate startDate = LocalDate.now();
                    LocalDate endDate = startDate.plusDays(30);
                    List<Reservation> upcomingReservations = reservationService.findUpcomingReservations(startDate, endDate);
                    model.addAttribute("upcomingReservations", upcomingReservations);
                    model.addAttribute("startDate", startDate);
                    model.addAttribute("endDate", endDate);
                    break;
            }
        }
        
        model.addAttribute("reportTypes", List.of("occupancy", "revenue", "upcoming"));
        model.addAttribute("selectedReportType", reportType);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);
        
        return "admin/reports";
    }
}
