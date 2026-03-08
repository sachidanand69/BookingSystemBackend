
package com.example.booking.repository;

import com.example.booking.entity.Slot;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Slot s where s.id = :id")
    Slot findSlotForUpdate(@Param("id") Long id);
}
