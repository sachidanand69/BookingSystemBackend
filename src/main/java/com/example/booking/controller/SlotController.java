package com.example.booking.controller;

import com.example.booking.entity.ResponseStructure;
import com.example.booking.entity.Slot;
import com.example.booking.repository.SlotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/slots")
public class SlotController {

    private final SlotRepository slotRepository;

    public SlotController(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @PostMapping
    public ResponseEntity<ResponseStructure<Slot>> createSlot(){

        ResponseStructure<Slot> response = new ResponseStructure<>();

        Slot slot = new Slot();

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);

        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setStatus(Slot.Status.AVAILABLE);

        Slot savedSlot = slotRepository.save(slot);

        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("Slot created successfully");
        response.setData(savedSlot);


        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // USER/ADMIN - Get slots
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Slot>>> getSlots(){

        ResponseStructure<List<Slot>> response = new ResponseStructure<>();

        List<Slot> slots = slotRepository.findAll();

        if(slots.isEmpty()){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("No slots found");
            response.setData(null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Slots fetched successfully");
        response.setData(slots);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteSlot(@PathVariable Long id){

        ResponseStructure<String> response = new ResponseStructure<>();

        Optional<Slot> optionalSlot = slotRepository.findById(id);

        if(optionalSlot.isEmpty()){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("Slot not found");
            response.setData(null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        slotRepository.deleteById(id);

        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Slot deleted successfully");
        response.setData("Deleted Slot ID: " + id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
