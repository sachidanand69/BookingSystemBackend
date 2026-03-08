
package com.example.booking.service;

import com.example.booking.entity.*;
import com.example.booking.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BookingService {

    private final SlotRepository slotRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingService(SlotRepository slotRepository, BookingRepository bookingRepository, UserRepository userRepository) {
        this.slotRepository = slotRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository=userRepository;
    }

    @Transactional
    public ResponseEntity<ResponseStructure<Booking>> bookSlot(Long slotId, Long userId) {

        ResponseStructure<Booking> response = new ResponseStructure<>();

        Slot slot = slotRepository.findSlotForUpdate(slotId);

        if (slot == null) {
            response.setStatus(404);
            response.setMessage("Slot not found");
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (slot.getStatus() == Slot.Status.BOOKED) {
            response.setStatus(400);
            response.setMessage("Slot already booked");
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            response.setStatus(404);
            response.setMessage("User not found");
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        if (user.getRole().equals("ADMIN")) {
            response.setStatus(403);
            response.setMessage("Admin users are not allowed to perform this action");
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        slot.setStatus(Slot.Status.BOOKED);

        Booking booking = new Booking();
        booking.setSlot(slot);
        booking.setUser(user);
        booking.setStatus(Booking.Status.ACTIVE);
        booking.setCreatedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        response.setStatus(200);
        response.setMessage("Slot booked successfully");
        response.setData(savedBooking);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
