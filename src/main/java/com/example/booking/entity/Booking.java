package com.example.booking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Booking{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship with User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Relationship with Slot
    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;

    public enum Status {
        ACTIVE,
        CANCELLED
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Slot getSlot() {
        return slot;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
