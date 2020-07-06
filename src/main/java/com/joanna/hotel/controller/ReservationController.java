package com.joanna.hotel.controller;

import com.joanna.hotel.dto.ReservationCreationDto;
import com.joanna.hotel.dto.ReservationDto;
import com.joanna.hotel.service.ReservationService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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

    @GetMapping(value = "/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReservationDto getReservationById(@PathVariable Long reservationId) {
        return reservationService.findById(reservationId);
    }

    @GetMapping(params = {"roomNumber"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDto> getReservationByRoomNumber(@RequestParam Integer roomNumber) {
        return reservationService.findByRoomNumber(roomNumber);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveReservation(@Valid @RequestBody ReservationCreationDto reservationCreationDto) {
        reservationService.save(reservationCreationDto);
    }

    @PutMapping(value = "/{reservationId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ReservationDto updateReservation(@PathVariable Long reservationId, @Valid @RequestBody ReservationCreationDto reservationCreationDto) {
        return reservationService.updateReservation(reservationId, reservationCreationDto);
    }

    @DeleteMapping(value = "/{reservationId}")
    public void deleteReservation(@PathVariable Long reservationId) {
        reservationService.delete(reservationId);
    }
}
