# Hotel Reservation System

A comprehensive hotel reservation management system built with Java Spring Boot, MySQL, and Thymeleaf. This system provides separate interfaces for guests and administrators with modern, responsive UI design.

## Features

### Guest Features
- **Room Search & Filtering**: Search available rooms by date range, room type, and price range
- **Room Booking**: Select rooms, specify dates, provide details, and confirm bookings
- **Reservation Management**: View, modify, and cancel existing reservations
- **Profile Management**: Update personal information and contact details
- **Cancellation Policy**: Free cancellation up to 24 hours before check-in

### Admin Features
- **Room Management**: Add, edit, and remove rooms with detailed information
- **Reservation Management**: View, search, edit, confirm, and cancel all reservations
- **User Management**: Manage customer accounts and user information
- **Reports & Analytics**: Generate occupancy reports, revenue reports, and booking summaries
- **Dashboard**: Real-time overview of hotel operations and statistics

### Technical Features
- **Modern UI**: Responsive design with Bootstrap 5 and custom CSS
- **Authentication**: Secure login with role-based access control
- **Database**: MySQL with JPA/Hibernate for data persistence
- **Validation**: Comprehensive form validation and error handling
- **Security**: Spring Security with password encryption
- **Responsive Design**: Works perfectly on all devices

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.0
- **Database**: MySQL 8.0
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript
- **Security**: Spring Security
- **Build Tool**: Maven
- **IDE**: Any Java IDE (IntelliJ IDEA, Eclipse, VS Code)

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- Git

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd hotel-reservation-system
```

### 2. Database Setup
1. Create a MySQL database named `hotel_reservation`
2. Update the database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### 4. Access the Application
- Open your browser and navigate to `http://localhost:8080`
- The application will automatically create sample data on first run

## Default Accounts

The system creates demo accounts automatically:

### Guest Account
- **Username**: `guest`
- **Password**: `password`
- **Email**: `guest@example.com`

### Admin Account
- **Username**: `admin`
- **Password**: `password`
- **Email**: `admin@hotel.com`

## Project Structure

```
src/
├── main/
│   ├── java/com/hotel/reservation/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST and MVC controllers
│   │   ├── entity/         # JPA entities
│   │   ├── repository/     # Data access layer
│   │   ├── service/        # Business logic layer
│   │   └── HotelReservationSystemApplication.java
│   └── resources/
│       ├── static/          # CSS, JS, images
│       ├── templates/       # Thymeleaf templates
│       └── application.properties
└── test/                    # Test files
```

## Key Components

### Entities
- **User**: Represents both guests and administrators
- **Room**: Hotel room information with amenities
- **Reservation**: Booking details and status

### Controllers
- **MainController**: Public pages (home, rooms, booking)
- **AuthController**: Login and registration
- **GuestController**: Guest-specific functionality
- **AdminController**: Administrative functions

### Services
- **UserService**: User management and authentication
- **RoomService**: Room operations and availability
- **ReservationService**: Booking and reservation management

## API Endpoints

### Public Endpoints
- `GET /` - Home page
- `GET /rooms` - Room listing
- `GET /rooms/{id}` - Room details
- `GET /login` - Login page
- `GET /register` - Registration page

### Guest Endpoints
- `GET /guest/rooms` - Guest room view
- `GET /guest/reservations` - My reservations
- `GET /guest/profile` - Profile management
- `POST /rooms/{id}/book` - Book a room

### Admin Endpoints
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/rooms` - Room management
- `GET /admin/reservations` - Reservation management
- `GET /admin/users` - User management
- `GET /admin/reports` - Reports and analytics

## Database Schema

### Users Table
- User information, roles, and authentication data
- Supports both GUEST and ADMIN roles

### Rooms Table
- Room details, pricing, amenities, and availability
- Room types: SINGLE, DOUBLE, SUITE, DELUXE, PRESIDENTIAL

### Reservations Table
- Booking information, dates, status, and pricing
- Status: PENDING, CONFIRMED, CANCELLED, COMPLETED, NO_SHOW

## Customization

### Adding New Room Types
1. Update the `RoomType` enum in `Room.java`
2. Add corresponding display names
3. Update templates to include new types

### Modifying UI Theme
1. Edit `src/main/resources/static/css/style.css`
2. Update CSS variables in `:root` selector
3. Modify Bootstrap theme colors

### Adding New Features
1. Create new entity classes
2. Add repository interfaces
3. Implement service layer
4. Create controllers
5. Design Thymeleaf templates

## Security Features

- Password encryption using BCrypt
- Role-based access control
- CSRF protection (disabled for development)
- Session management
- Input validation and sanitization

## Performance Optimizations

- Lazy loading for JPA entities
- Efficient database queries
- Responsive image loading
- Minified CSS and JavaScript
- Database connection pooling

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify MySQL is running
   - Check credentials in `application.properties`
   - Ensure database exists

2. **Port Already in Use**
   - Change port in `application.properties`
   - Kill existing process on port 8080

3. **Build Failures**
   - Ensure Java 17 is installed
   - Check Maven version compatibility
   - Clear Maven cache: `mvn clean`

### Logs
- Application logs are available in console
- Enable debug logging by setting `logging.level.com.hotel.reservation=DEBUG`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation

## Future Enhancements

- [ ] Payment integration
- [ ] Email notifications
- [ ] Mobile app
- [ ] Advanced reporting
- [ ] Multi-language support
- [ ] API documentation
- [ ] Unit and integration tests
- [ ] Docker containerization
- [ ] CI/CD pipeline

---

**Hotel Reservation System** - Built with ❤️ using Spring Boot
