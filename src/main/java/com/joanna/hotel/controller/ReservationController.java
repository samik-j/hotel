package com.joanna.hotel.controller;

import com.joanna.hotel.dto.ReservationDto;
import com.joanna.hotel.exception.NoRoomsAvailableException;
import com.joanna.hotel.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDto> getAllReservations() {
        return reservationService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveReservation(@Valid @RequestBody ReservationDto reservationDto) {
        reservationService.save(reservationDto);
    }
}
