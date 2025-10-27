package com.hotel.reservation.service;

import com.hotel.reservation.entity.User;
import com.hotel.reservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return user;
    }
    
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public User updateUser(User user) {
        // Get the existing user to preserve password if not provided
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Only update password if a new one is provided
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // Keep the existing password
            user.setPassword(existingUser.getPassword());
        }
        
        return userRepository.save(user);
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public List<User> findAllAdmins() {
        return userRepository.findAllAdmins();
    }
    
    public List<User> findAllGuests() {
        return userRepository.findAllGuests();
    }
    
    public List<User> findByNameContaining(String name) {
        return userRepository.findByNameContaining(name);
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check for active reservations
        if (!user.getReservations().isEmpty()) {
            boolean hasActiveReservations = user.getReservations().stream()
                    .anyMatch(reservation -> reservation.getStatus() == 
                            com.hotel.reservation.entity.Reservation.ReservationStatus.CONFIRMED ||
                            reservation.getStatus() == 
                            com.hotel.reservation.entity.Reservation.ReservationStatus.PENDING);
            
            if (hasActiveReservations) {
                throw new RuntimeException("Cannot delete user with active reservations");
            }
        }
        
        userRepository.deleteById(id);
    }
    
    public User registerGuest(User user) {
        user.setRole(User.Role.GUEST);
        return saveUser(user);
    }
    
    public User createAdmin(User user) {
        user.setRole(User.Role.ADMIN);
        return saveUser(user);
    }
}
