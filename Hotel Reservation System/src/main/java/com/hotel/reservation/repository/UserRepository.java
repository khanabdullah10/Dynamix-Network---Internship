package com.hotel.reservation.repository;

import com.hotel.reservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.role = 'ADMIN'")
    java.util.List<User> findAllAdmins();
    
    @Query("SELECT u FROM User u WHERE u.role = 'GUEST'")
    java.util.List<User> findAllGuests();
    
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    java.util.List<User> findByNameContaining(@Param("name") String name);
}
