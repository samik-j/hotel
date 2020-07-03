package com.joanna.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joanna.hotel.validation.StartBeforeEndDateConstraint;
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
@StartBeforeEndDateConstraint
public class ReservationCreationDto {

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
}
