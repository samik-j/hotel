package com.joanna.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joanna.hotel.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservationDto {

    @NotNull
    private Long id;

    @NotBlank
    private String userName;

    @NotNull
    @Min(1)
    private Integer numberOfPeople;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull
    private Integer roomNumber;

    public ReservationDto(Reservation reservation) {
        this.id = reservation.getId();
        this.userName = reservation.getUserName();
        this.numberOfPeople = reservation.getNumberOfPeople();
        this.startDate = reservation.getStartDate();
        this.endDate = reservation.getEndDate();
        this.roomNumber = reservation.getRoom().getRoomNumber();
    }
}
