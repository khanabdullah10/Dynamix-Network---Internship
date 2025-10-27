package com.hotel.reservation.config;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.entity.User;
import com.hotel.reservation.service.ReservationService;
import com.hotel.reservation.service.RoomService;
import com.hotel.reservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private ReservationService reservationService;
    
    @Override
    public void run(String... args) throws Exception {
        // Only initialize data if no users exist
        if (userService.findAll().isEmpty()) {
            initializeUsers();
            initializeRooms();
            initializeReservations();
        }
    }
    
    private void initializeUsers() {
        // Create admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@hotel.com");
        admin.setPassword("password");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setPhoneNumber("+1-555-0001");
        admin.setRole(User.Role.ADMIN);
        userService.createAdmin(admin);
        
        // Create guest users
        User guest1 = new User();
        guest1.setUsername("guest");
        guest1.setEmail("guest@example.com");
        guest1.setPassword("password");
        guest1.setFirstName("John");
        guest1.setLastName("Doe");
        guest1.setPhoneNumber("+1-555-0002");
        guest1.setRole(User.Role.GUEST);
        userService.registerGuest(guest1);
        
        User guest2 = new User();
        guest2.setUsername("jane.smith");
        guest2.setEmail("jane.smith@example.com");
        guest2.setPassword("password");
        guest2.setFirstName("Jane");
        guest2.setLastName("Smith");
        guest2.setPhoneNumber("+1-555-0003");
        guest2.setRole(User.Role.GUEST);
        userService.registerGuest(guest2);
        
        User guest3 = new User();
        guest3.setUsername("mike.wilson");
        guest3.setEmail("mike.wilson@example.com");
        guest3.setPassword("password");
        guest3.setFirstName("Mike");
        guest3.setLastName("Wilson");
        guest3.setPhoneNumber("+1-555-0004");
        guest3.setRole(User.Role.GUEST);
        userService.registerGuest(guest3);
    }
    
    private void initializeRooms() {
        // Single Rooms
        Room room101 = new Room();
        room101.setRoomNumber("101");
        room101.setRoomType(Room.RoomType.SINGLE);
        room101.setDescription("Cozy single room with city view, perfect for business travelers. Features a comfortable queen bed, work desk, and modern amenities.");
        room101.setPricePerNight(new BigDecimal("120.00"));
        room101.setMaxOccupancy(1);
        room101.setHasWifi(true);
        room101.setHasTv(true);
        room101.setHasAc(true);
        room101.setHasMinibar(false);
        room101.setHasBalcony(false);
        room101.setIsAvailable(true);
        room101.setImageUrl("https://images.unsplash.com/photo-1631049307264-da0ec9d70304?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80");
        roomService.saveRoom(room101);
        
        Room room102 = new Room();
        room102.setRoomNumber("102");
        room102.setRoomType(Room.RoomType.SINGLE);
        room102.setDescription("Elegant single room with garden view. Includes premium bedding, spacious bathroom, and complimentary amenities.");
        room102.setPricePerNight(new BigDecimal("130.00"));
        room102.setMaxOccupancy(1);
        room102.setHasWifi(true);
        room102.setHasTv(true);
        room102.setHasAc(true);
        room102.setHasMinibar(true);
        room102.setHasBalcony(true);
        room102.setIsAvailable(true);
        room102.setImageUrl("https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80");
        roomService.saveRoom(room102);
        
        // Double Rooms
        Room room201 = new Room();
        room201.setRoomNumber("201");
        room201.setRoomType(Room.RoomType.DOUBLE);
        room201.setDescription("Spacious double room with two queen beds, ideal for families or groups. Features modern decor and city skyline views.");
        room201.setPricePerNight(new BigDecimal("180.00"));
        room201.setMaxOccupancy(4);
        room201.setHasWifi(true);
        room201.setHasTv(true);
        room201.setHasAc(true);
        room201.setHasMinibar(true);
        room201.setHasBalcony(true);
        room201.setIsAvailable(true);
        room201.setImageUrl("https://images.unsplash.com/photo-1611892440504-42a792e24d32?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80");
        roomService.saveRoom(room201);
        
        Room room202 = new Room();
        room202.setRoomNumber("202");
        room202.setRoomType(Room.RoomType.DOUBLE);
        room202.setDescription("Premium double room with king bed and separate sitting area. Perfect for romantic getaways or extended stays.");
        room202.setPricePerNight(new BigDecimal("200.00"));
        room202.setMaxOccupancy(2);
        room202.setHasWifi(true);
        room202.setHasTv(true);
        room202.setHasAc(true);
        room202.setHasMinibar(true);
        room202.setHasBalcony(true);
        room202.setIsAvailable(true);
        room202.setImageUrl("https://images.unsplash.com/photo-1595576508898-0ad5c879a061?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80");
        roomService.saveRoom(room202);
        
        // Suite Rooms
        Room room301 = new Room();
        room301.setRoomNumber("301");
        room301.setRoomType(Room.RoomType.SUITE);
        room301.setDescription("Luxurious suite with separate bedroom and living area. Features panoramic city views, premium amenities, and personalized service.");
        room301.setPricePerNight(new BigDecimal("350.00"));
        room301.setMaxOccupancy(4);
        room301.setHasWifi(true);
        room301.setHasTv(true);
        room301.setHasAc(true);
        room301.setHasMinibar(true);
        room301.setHasBalcony(true);
        room301.setIsAvailable(true);
        room301.setImageUrl("https://images.unsplash.com/photo-1578683010236-d716f9a3f461?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80");
        roomService.saveRoom(room301);
        
        Room room302 = new Room();
        room302.setRoomNumber("302");
        room302.setRoomType(Room.RoomType.SUITE);
        room302.setDescription("Executive suite with private balcony, marble bathroom, and premium furnishings. Includes complimentary breakfast and concierge service.");
        room302.setPricePerNight(new BigDecimal("400.00"));
        room302.setMaxOccupancy(2);
        room302.setHasWifi(true);
        room302.setHasTv(true);
        room302.setHasAc(true);
        room302.setHasMinibar(true);
        room302.setHasBalcony(true);
        room302.setIsAvailable(true);
        room302.setImageUrl("https://images.unsplash.com/photo-1590490360182-c33d57733427?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80");
        roomService.saveRoom(room302);
        
        // Deluxe Rooms
        Room room401 = new Room();
        room401.setRoomNumber("401");
        room401.setRoomType(Room.RoomType.DELUXE);
        room401.setDescription("Deluxe room with premium amenities and stunning ocean views. Features a king bed, marble bathroom, and private terrace.");
        room401.setPricePerNight(new BigDecimal("280.00"));
        room401.setMaxOccupancy(2);
        room401.setHasWifi(true);
        room401.setHasTv(true);
        room401.setHasAc(true);
        room401.setHasMinibar(true);
        room401.setHasBalcony(true);
        room401.setIsAvailable(true);
        room401.setImageUrl("https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80");
        roomService.saveRoom(room401);
        
        Room room402 = new Room();
        room402.setRoomNumber("402");
        room402.setRoomType(Room.RoomType.DELUXE);
        room402.setDescription("Spacious deluxe room with modern design and premium finishes. Includes a comfortable seating area and city views.");
        room402.setPricePerNight(new BigDecimal("300.00"));
        room402.setMaxOccupancy(3);
        room402.setHasWifi(true);
        room402.setHasTv(true);
        room402.setHasAc(true);
        room402.setHasMinibar(true);
        room402.setHasBalcony(false);
        room402.setIsAvailable(true);
        room402.setImageUrl("https://images.unsplash.com/photo-1571896349842-33c89424de2d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80");
        roomService.saveRoom(room402);
        
        // Presidential Suite
        Room room501 = new Room();
        room501.setRoomNumber("501");
        room501.setRoomType(Room.RoomType.PRESIDENTIAL);
        room501.setDescription("Ultimate luxury in our Presidential Suite. Features multiple rooms, private dining area, butler service, and panoramic city views.");
        room501.setPricePerNight(new BigDecimal("800.00"));
        room501.setMaxOccupancy(6);
        room501.setHasWifi(true);
        room501.setHasTv(true);
        room501.setHasAc(true);
        room501.setHasMinibar(true);
        room501.setHasBalcony(true);
        room501.setIsAvailable(true);
        room501.setImageUrl("https://images.unsplash.com/photo-1598300042247-d088f8ab3a91?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80");
        roomService.saveRoom(room501);
    }
    
    private void initializeReservations() {
        // Get users and rooms
        User guest1 = userService.findByUsername("guest").orElse(null);
        User guest2 = userService.findByUsername("jane.smith").orElse(null);
        User guest3 = userService.findByUsername("mike.wilson").orElse(null);
        
        Room room101 = roomService.findByRoomNumber("101").orElse(null);
        Room room201 = roomService.findByRoomNumber("201").orElse(null);
        Room room301 = roomService.findByRoomNumber("301").orElse(null);
        
        if (guest1 != null && room101 != null) {
            try {
                Reservation reservation1 = reservationService.createReservation(
                    guest1, room101, 
                    LocalDate.now().plusDays(7), 
                    LocalDate.now().plusDays(10), 
                    1, 
                    "Please provide extra towels"
                );
                reservationService.confirmReservation(reservation1.getId());
            } catch (Exception e) {
                System.err.println("Failed to create reservation 1: " + e.getMessage());
            }
        }
        
        if (guest2 != null && room201 != null) {
            try {
                Reservation reservation2 = reservationService.createReservation(
                    guest2, room201, 
                    LocalDate.now().plusDays(14), 
                    LocalDate.now().plusDays(17), 
                    2, 
                    "Anniversary celebration"
                );
                reservationService.confirmReservation(reservation2.getId());
            } catch (Exception e) {
                System.err.println("Failed to create reservation 2: " + e.getMessage());
            }
        }
        
        if (guest3 != null && room301 != null) {
            try {
                Reservation reservation3 = reservationService.createReservation(
                    guest3, room301, 
                    LocalDate.now().plusDays(21), 
                    LocalDate.now().plusDays(25), 
                    2, 
                    "Business trip with client meetings"
                );
                // Keep this one as pending - reservation3 is intentionally not confirmed
                System.out.println("Created pending reservation: " + reservation3.getConfirmationNumber());
            } catch (Exception e) {
                System.err.println("Failed to create reservation 3: " + e.getMessage());
            }
        }
    }
}
