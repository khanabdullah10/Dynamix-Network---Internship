package com.hotel.reservation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Room number is required")
    @Size(max = 10, message = "Room number must not exceed 10 characters")
    @Column(name = "room_number", unique = true, nullable = false)
    private String roomNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType roomType;
    
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal pricePerNight;
    
    @Column(name = "max_occupancy", nullable = false)
    private Integer maxOccupancy = 1;
    
    @Column(name = "has_wifi")
    private Boolean hasWifi = true;
    
    @Column(name = "has_tv")
    private Boolean hasTv = true;
    
    @Column(name = "has_ac")
    private Boolean hasAc = true;
    
    @Column(name = "has_minibar")
    private Boolean hasMinibar = false;
    
    @Column(name = "has_balcony")
    private Boolean hasBalcony = false;
    
    @Column(name = "is_available")
    private Boolean isAvailable = true;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Room() {}
    
    public Room(String roomNumber, RoomType roomType, String description, BigDecimal pricePerNight, Integer maxOccupancy) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.maxOccupancy = maxOccupancy;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public RoomType getRoomType() {
        return roomType;
    }
    
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }
    
    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
    
    public Integer getMaxOccupancy() {
        return maxOccupancy;
    }
    
    public void setMaxOccupancy(Integer maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }
    
    public Boolean getHasWifi() {
        return hasWifi;
    }
    
    public void setHasWifi(Boolean hasWifi) {
        this.hasWifi = hasWifi;
    }
    
    public Boolean getHasTv() {
        return hasTv;
    }
    
    public void setHasTv(Boolean hasTv) {
        this.hasTv = hasTv;
    }
    
    public Boolean getHasAc() {
        return hasAc;
    }
    
    public void setHasAc(Boolean hasAc) {
        this.hasAc = hasAc;
    }
    
    public Boolean getHasMinibar() {
        return hasMinibar;
    }
    
    public void setHasMinibar(Boolean hasMinibar) {
        this.hasMinibar = hasMinibar;
    }
    
    public Boolean getHasBalcony() {
        return hasBalcony;
    }
    
    public void setHasBalcony(Boolean hasBalcony) {
        this.hasBalcony = hasBalcony;
    }
    
    public Boolean getIsAvailable() {
        return isAvailable;
    }
    
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<Reservation> getReservations() {
        return reservations;
    }
    
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    public static enum RoomType {
        SINGLE("Single Room"),
        DOUBLE("Double Room"),
        SUITE("Suite"),
        DELUXE("Deluxe Room"),
        PRESIDENTIAL("Presidential Suite");
        
        private final String displayName;
        
        RoomType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
