# Slot Booking System (Spring Boot)

## Project Overview

This project is a Spring Boot based Slot Booking System that allows
users to: - Register and login - View available slots - Book a slot -
Prevent multiple users from booking the same slot at the same time

The system demonstrates how to safely handle concurrent booking requests
using transaction management and database locking.

------------------------------------------------------------------------

# Tech Stack

-   Java 17
-   Spring Boot
-   Spring Data JPA
-   Spring Security
-   JWT Authentication
-   H2 Database
-   Maven
-   JUnit 5
-   Mockito

------------------------------------------------------------------------

# Locking Strategy

To prevent two users from booking the same slot simultaneously,
**Pessimistic Locking** is used.

Pessimistic locking ensures that when a transaction reads a slot for
booking, the database row is locked so that other transactions cannot
modify it until the first transaction finishes.

Example:

@Lock(LockModeType.PESSIMISTIC_WRITE) @Query("SELECT s FROM Slot s WHERE
s.id = :id") Optional`<Slot>`{=html} findSlotForUpdate(Long id);

This guarantees that only one transaction can update the slot at a time.

------------------------------------------------------------------------

# Transaction Boundaries

Transactions are managed using Spring's **@Transactional** annotation in
the service layer.

Example:

@Transactional public Booking bookSlot(Long slotId, Long userId) {

    Slot slot = slotRepository.findSlotForUpdate(slotId)
        .orElseThrow(() -> new RuntimeException("Slot not found"));

    if (slot.isBooked()) {
        throw new RuntimeException("Slot already booked");
    }

    slot.setBooked(true);
    slotRepository.save(slot);

    Booking booking = new Booking(userId, slotId);
    return bookingRepository.save(booking);

}

Transaction flow:

1.  Transaction starts
2.  Slot row is locked
3.  Booking validation is performed
4.  Slot is marked as booked
5.  Booking record is saved
6.  Transaction commits

------------------------------------------------------------------------

# Race Condition Prevention

Race conditions occur when multiple users attempt to book the same slot
simultaneously.

Prevention techniques used in this project:

1.  Database row locking using pessimistic lock
2.  Transactional service layer
3.  Slot availability validation before booking

Execution flow:

User A requests slot User B requests same slot

User A -\> acquires lock User B -\> waits

User A -\> booking success User B -\> slot already booked

------------------------------------------------------------------------

# Example Concurrent Booking Scenario

Initial State: Slot 101 → Available

Two users try booking at the same time.

User A → Book Slot 101 User B → Book Slot 101

Execution:

Step 1: User A starts transaction\
Step 2: Slot 101 is locked\
Step 3: User B waits for lock\
Step 4: User A completes booking\
Step 5: Transaction commits\
Step 6: User B resumes execution\
Step 7: Slot already booked → booking rejected

Final Result:

User A → SUCCESS\
User B → FAILED (Slot already booked)

------------------------------------------------------------------------

# API Endpoints

Authentication

POST /auth/create → Register user\
POST /auth/login → Login user

Slot APIs

POST /slots → Create slot\
GET /slots → View slots

Booking API

POST /bookings?slotId={slotId}&userId={userId} → Book slot

------------------------------------------------------------------------

# Steps to Run the Application

1.  Clone the repository

git clone https://github.com/sachidanand69/BookingSystemBackend.git

2.  Navigate to the project folder

cd booking-system

3.  Build the project

mvn clean install

4.  Run the application

mvn spring-boot:run

Application runs at:

http://localhost:8081

------------------------------------------------------------------------

# Database

The project uses H2 in-memory database.

H2 console:

http://localhost:8081/h2-console

------------------------------------------------------------------------

# Unit Testing

Unit tests are implemented using **JUnit 5** and **Mockito**.

The project targets **at least 80% test coverage**.

Example unit test:

@Test void testBookSlotSuccess() {

    Slot slot = new Slot(1L, false);

    when(slotRepository.findSlotForUpdate(1L))
        .thenReturn(Optional.of(slot));

    Booking booking = bookingService.bookSlot(1L, 1L);

    assertNotNull(booking);

}

Tested components:

-   User APIs
-   Slot APIs
-   Booking APIs
-   Service layer

------------------------------------------------------------------------

# Author

Sachidanand Behera