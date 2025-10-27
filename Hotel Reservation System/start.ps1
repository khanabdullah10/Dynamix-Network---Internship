Write-Host "🏨 Hotel Reservation System" -ForegroundColor Cyan
Write-Host "=========================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Starting the application..." -ForegroundColor Green
Write-Host ""
Write-Host "Prerequisites:" -ForegroundColor Yellow
Write-Host "1. Java 17 or higher ✓" -ForegroundColor Green
Write-Host "2. MySQL database running" -ForegroundColor Yellow
Write-Host "3. Database 'hotel_reservation' created" -ForegroundColor Yellow
Write-Host ""
Write-Host "Running options:" -ForegroundColor Yellow
Write-Host "1. With Maven: mvn spring-boot:run" -ForegroundColor Cyan
Write-Host "2. With IDE: Run HotelReservationSystemApplication.java" -ForegroundColor Cyan
Write-Host ""
Write-Host "Application URL: http://localhost:8080" -ForegroundColor Magenta
Write-Host ""
Write-Host "Demo Accounts:" -ForegroundColor Yellow
Write-Host "  Guest: username=guest, password=password" -ForegroundColor Green
Write-Host "  Admin: username=admin, password=password" -ForegroundColor Green
Write-Host ""

# Try to run with Maven if available
try {
    Write-Host "Attempting to run with Maven..." -ForegroundColor Yellow
    mvn spring-boot:run
} catch {
    Write-Host "Maven not found. Please use one of the following methods:" -ForegroundColor Red
    Write-Host ""
    Write-Host "Method 1 - Install Maven:" -ForegroundColor Yellow
    Write-Host "  1. Download from: https://maven.apache.org/download.cgi"
    Write-Host "  2. Add to PATH"
    Write-Host "  3. Run: mvn spring-boot:run"
    Write-Host ""
    Write-Host "Method 2 - Use IDE:" -ForegroundColor Yellow
    Write-Host "  1. Open project in IntelliJ IDEA or Eclipse"
    Write-Host "  2. Run HotelReservationSystemApplication.java"
    Write-Host ""
    Write-Host "Method 3 - Manual Setup:" -ForegroundColor Yellow
    Write-Host "  1. Download Spring Boot CLI"
    Write-Host "  2. Run: spring run src/main/java/com/hotel/reservation/HotelReservationSystemApplication.java"
}

Write-Host ""
Write-Host "Press any key to continue..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
