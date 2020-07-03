package com.joanna.hotel.repository;

import com.joanna.hotel.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    void deleteById(Long id);
}
