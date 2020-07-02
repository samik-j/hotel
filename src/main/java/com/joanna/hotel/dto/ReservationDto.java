package com.joanna.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReservationDto {

    @NotBlank
    private String userName;

    @NotNull
    @Size(min = 1)
    private Integer numberOfPeople;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
