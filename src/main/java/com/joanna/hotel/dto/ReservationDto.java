package com.joanna.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @NotBlank
    private String userName;

    @NotNull
    @Min(1)
    private Integer numberOfPeople;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
