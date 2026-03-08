
package com.example.booking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Version
    private Long version;

    public enum Status {
        AVAILABLE, BOOKED
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
