package com.joanna.hotel.validation;

import com.joanna.hotel.dto.ReservationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartBeforeEndDateValidator implements ConstraintValidator<StartBeforeEndDateConstraint, ReservationDto> {

    @Override
    public void initialize(StartBeforeEndDateConstraint startBeforeEndDateConstraint) {
    }

    @Override
    public boolean isValid(ReservationDto reservationDto, ConstraintValidatorContext constraintValidatorContext) {
        if (reservationDto.getStartDate() == null && reservationDto.getEndDate() == null) {
            return true;
        }
        return reservationDto.getStartDate().isBefore(reservationDto.getEndDate());
    }
}
