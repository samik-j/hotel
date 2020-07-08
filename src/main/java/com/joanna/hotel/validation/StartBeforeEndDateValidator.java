package com.joanna.hotel.validation;

import com.joanna.hotel.dto.ReservationCreationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartBeforeEndDateValidator implements ConstraintValidator<StartBeforeEndDateConstraint, ReservationCreationDto> {

    @Override
    public void initialize(StartBeforeEndDateConstraint startBeforeEndDateConstraint) {
    }

    @Override
    public boolean isValid(ReservationCreationDto reservationCreationDto, ConstraintValidatorContext constraintValidatorContext) {
        if (reservationCreationDto.getStartDate() == null && reservationCreationDto.getEndDate() == null) {
            return true;
        }
        return reservationCreationDto.getStartDate() != null
                && reservationCreationDto.getEndDate() != null
                && reservationCreationDto.getStartDate().isBefore(reservationCreationDto.getEndDate());
    }
}
