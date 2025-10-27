// Hotel Reservation System - Custom JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Form validation enhancement
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Loading spinner for forms
    const submitButtons = document.querySelectorAll('button[type="submit"]');
    submitButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            const form = this.closest('form');
            if (form && form.checkValidity()) {
                this.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status"></span>Processing...';
                this.disabled = true;
            }
        });
    });

    // Date picker enhancements
    const dateInputs = document.querySelectorAll('input[type="date"]');
    dateInputs.forEach(function(input) {
        // Set minimum date to today
        const today = new Date().toISOString().split('T')[0];
        input.min = today;

        // Add change event for check-in/check-out date dependencies
        if (input.id === 'checkInDate') {
            input.addEventListener('change', function() {
                const checkOutInput = document.getElementById('checkOutDate');
                if (checkOutInput) {
                    const checkInDate = new Date(this.value);
                    checkInDate.setDate(checkInDate.getDate() + 1);
                    checkOutInput.min = checkInDate.toISOString().split('T')[0];
                    
                    // If check-out date is before new minimum, update it
                    if (checkOutInput.value && new Date(checkOutInput.value) <= checkInDate) {
                        checkOutInput.value = checkInDate.toISOString().split('T')[0];
                    }
                }
            });
        }
    });

    // Room card hover effects
    const roomCards = document.querySelectorAll('.room-card');
    roomCards.forEach(function(card) {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });

    // Search functionality
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        let searchTimeout;
        searchInput.addEventListener('input', function() {
            clearTimeout(searchTimeout);
            searchTimeout = setTimeout(function() {
                performSearch(searchInput.value);
            }, 300);
        });
    }

    // Confirmation dialogs for delete actions
    const deleteButtons = document.querySelectorAll('[data-confirm-delete]');
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const message = this.getAttribute('data-confirm-message') || 'Are you sure you want to delete this item?';
            if (confirm(message)) {
                const form = this.closest('form');
                if (form) {
                    form.submit();
                } else {
                    window.location.href = this.href;
                }
            }
        });
    });

    // Status badge animations
    const statusBadges = document.querySelectorAll('.badge');
    statusBadges.forEach(function(badge) {
        badge.addEventListener('mouseenter', function() {
            this.style.transform = 'scale(1.1)';
        });
        
        badge.addEventListener('mouseleave', function() {
            this.style.transform = 'scale(1)';
        });
    });

    // Table row hover effects
    const tableRows = document.querySelectorAll('tbody tr');
    tableRows.forEach(function(row) {
        row.addEventListener('mouseenter', function() {
            this.style.backgroundColor = '#f8f9fa';
        });
        
        row.addEventListener('mouseleave', function() {
            this.style.backgroundColor = '';
        });
    });

    // Image lazy loading
    const images = document.querySelectorAll('img[data-src]');
    const imageObserver = new IntersectionObserver(function(entries, observer) {
        entries.forEach(function(entry) {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                img.classList.remove('lazy');
                imageObserver.unobserve(img);
            }
        });
    });

    images.forEach(function(img) {
        imageObserver.observe(img);
    });

    // Price formatting
    const priceElements = document.querySelectorAll('.price');
    priceElements.forEach(function(element) {
        const price = parseFloat(element.textContent);
        if (!isNaN(price)) {
            element.textContent = '$' + price.toFixed(2);
        }
    });

    // Reservation status color coding
    const statusElements = document.querySelectorAll('.reservation-status');
    statusElements.forEach(function(element) {
        const status = element.textContent.toLowerCase();
        element.classList.add('badge');
        
        switch(status) {
            case 'pending':
                element.classList.add('bg-warning');
                break;
            case 'confirmed':
                element.classList.add('bg-success');
                break;
            case 'cancelled':
                element.classList.add('bg-danger');
                break;
            case 'completed':
                element.classList.add('bg-info');
                break;
            default:
                element.classList.add('bg-secondary');
        }
    });

    // Auto-refresh for dashboard data
    const dashboardElements = document.querySelectorAll('[data-auto-refresh]');
    dashboardElements.forEach(function(element) {
        const interval = parseInt(element.getAttribute('data-refresh-interval')) || 30000; // 30 seconds default
        setInterval(function() {
            fetch(element.getAttribute('data-refresh-url'))
                .then(response => response.text())
                .then(data => {
                    element.innerHTML = data;
                })
                .catch(error => console.error('Auto-refresh failed:', error));
        }, interval);
    });

    // Print functionality
    const printButtons = document.querySelectorAll('.print-btn');
    printButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            window.print();
        });
    });

    // Copy to clipboard functionality
    const copyButtons = document.querySelectorAll('.copy-btn');
    copyButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            const textToCopy = this.getAttribute('data-copy-text');
            navigator.clipboard.writeText(textToCopy).then(function() {
                // Show success message
                const originalText = button.innerHTML;
                button.innerHTML = '<i class="fas fa-check me-1"></i>Copied!';
                button.classList.add('btn-success');
                button.classList.remove('btn-outline-secondary');
                
                setTimeout(function() {
                    button.innerHTML = originalText;
                    button.classList.remove('btn-success');
                    button.classList.add('btn-outline-secondary');
                }, 2000);
            });
        });
    });
});

// Utility functions
function performSearch(query) {
    if (query.length < 2) return;
    
    fetch('/api/search?q=' + encodeURIComponent(query))
        .then(response => response.json())
        .then(data => {
            updateSearchResults(data);
        })
        .catch(error => console.error('Search failed:', error));
}

function updateSearchResults(results) {
    const resultsContainer = document.getElementById('searchResults');
    if (resultsContainer) {
        resultsContainer.innerHTML = '';
        
        if (results.length === 0) {
            resultsContainer.innerHTML = '<div class="alert alert-info">No results found</div>';
            return;
        }
        
        results.forEach(function(result) {
            const resultElement = document.createElement('div');
            resultElement.className = 'search-result-item p-3 border-bottom';
            resultElement.innerHTML = `
                <h6 class="mb-1">${result.title}</h6>
                <p class="text-muted mb-0">${result.description}</p>
            `;
            resultsContainer.appendChild(resultElement);
        });
    }
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

function formatDate(date) {
    return new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    }).format(new Date(date));
}

function calculateNights(checkInDate, checkOutDate) {
    const checkIn = new Date(checkInDate);
    const checkOut = new Date(checkOutDate);
    const timeDiff = checkOut.getTime() - checkIn.getTime();
    return Math.ceil(timeDiff / (1000 * 3600 * 24));
}

function validateDateRange(checkInDate, checkOutDate) {
    const checkIn = new Date(checkInDate);
    const checkOut = new Date(checkOutDate);
    const today = new Date();
    
    if (checkIn < today) {
        return 'Check-in date cannot be in the past';
    }
    
    if (checkOut <= checkIn) {
        return 'Check-out date must be after check-in date';
    }
    
    return null;
}

// Export functions for global use
window.HotelReservation = {
    formatCurrency,
    formatDate,
    calculateNights,
    validateDateRange
};
