
package com.example.booking.controller;

import com.example.booking.entity.Booking;
import com.example.booking.entity.ResponseStructure;
import com.example.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<ResponseStructure<Booking>> book(@RequestParam Long slotId, @RequestParam Long userId){
        return bookingService.bookSlot(slotId, userId);
    }
}
