package com.joanna.hotel.service;

import com.joanna.hotel.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

}
