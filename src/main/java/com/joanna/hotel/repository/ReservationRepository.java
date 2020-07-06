package com.joanna.hotel.repository;

import com.joanna.hotel.model.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Modifying
    @Query("DELETE FROM Reservation reservation WHERE reservation.id = :id")
    void deleteById(@Param("id") Long id);

    @Query("SELECT reservation FROM Reservation reservation WHERE reservation.room.roomNumber = :roomNumber")
    List<Reservation> findByRoomNumber(@Param("roomNumber") Integer roomNumber);
}
