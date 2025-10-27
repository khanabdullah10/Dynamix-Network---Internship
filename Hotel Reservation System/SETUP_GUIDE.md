# 🏨 Hotel Reservation System - Setup & Run Guide

## ✅ Project Status
Your Hotel Reservation System is **ready to run**! All errors have been fixed and the project compiles successfully.

## 🚀 How to Run the Application

### Option 1: Using Maven (Recommended)

1. **Install Maven:**
   - Download from: https://maven.apache.org/download.cgi
   - Extract to a folder (e.g., `C:\apache-maven-3.9.6`)
   - Add `C:\apache-maven-3.9.6\bin` to your system PATH
   - Restart your command prompt/PowerShell

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

### Option 2: Using IDE (Easiest)

1. **IntelliJ IDEA:**
   - Open the project folder
   - Wait for Maven dependencies to download
   - Right-click on `HotelReservationSystemApplication.java`
   - Select "Run 'HotelReservationSystemApplication'"

2. **Eclipse:**
   - Import as Maven project
   - Right-click on `HotelReservationSystemApplication.java`
   - Select "Run As" → "Java Application"

3. **VS Code:**
   - Install "Extension Pack for Java"
   - Open the project folder
   - Run the main class

### Option 3: Manual Setup

1. **Download Spring Boot CLI:**
   - From: https://spring.io/guides/gs/spring-boot/
   - Add to PATH

2. **Run directly:**
   ```bash
   spring run src/main/java/com/hotel/reservation/HotelReservationSystemApplication.java
   ```

## 🗄️ Database Setup

### Prerequisites:
1. **MySQL Server** must be running
2. **Create database:**
   ```sql
   CREATE DATABASE hotel_reservation;
   ```

### Configuration:
- Update `src/main/resources/application.properties` if needed:
  ```properties
  spring.datasource.username=your_username
  spring.datasource.password=your_password
  ```

## 🌐 Access the Application

Once running, open your browser and go to:
**http://localhost:8080**

## 👤 Demo Accounts

The system automatically creates sample data:

### Guest Account:
- **Username:** `guest`
- **Password:** `password`
- **Email:** `guest@example.com`

### Admin Account:
- **Username:** `admin`
- **Password:** `password`
- **Email:** `admin@hotel.com`

## 🎯 Features Available

### For Guests:
- ✅ Browse and search rooms
- ✅ Book reservations
- ✅ Manage profile
- ✅ Cancel reservations (24-hour policy)

### For Admins:
- ✅ Manage rooms (add, edit, delete)
- ✅ Manage reservations
- ✅ Manage users
- ✅ View reports and analytics
- ✅ Dashboard with statistics

## 🎨 UI Features

- ✅ **Responsive design** - works on all devices
- ✅ **Modern animations** - smooth transitions
- ✅ **Beautiful styling** - Bootstrap 5 + custom CSS
- ✅ **Interactive elements** - JavaScript enhancements
- ✅ **Professional look** - hotel industry standard

## 🔧 Troubleshooting

### Common Issues:

1. **Port 8080 already in use:**
   - Change port in `application.properties`
   - Or kill the process using port 8080

2. **Database connection error:**
   - Ensure MySQL is running
   - Check database credentials
   - Verify database exists

3. **Maven not found:**
   - Install Maven and add to PATH
   - Or use an IDE instead

4. **Java version error:**
   - Ensure Java 17+ is installed
   - Check JAVA_HOME environment variable

## 📱 Mobile Support

The application is fully responsive and works perfectly on:
- 📱 Mobile phones
- 📱 Tablets
- 💻 Desktop computers
- 🖥️ Large screens

## 🎉 Success!

Once the application starts, you'll see:
- Beautiful homepage with featured rooms
- Working authentication system
- Complete room booking functionality
- Admin dashboard with real-time data
- Sample rooms and reservations already created

**Enjoy your Hotel Reservation System!** 🏨✨
