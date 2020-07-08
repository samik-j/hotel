package com.joanna.hotel.validation;

import com.joanna.hotel.dto.ReservationCreationDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class StartBeforeEndDateValidatorTest {

    StartBeforeEndDateValidator validator = new StartBeforeEndDateValidator();

    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @Test
    public void startDateBeforeEndDateShouldBeValid() {
        ReservationCreationDto reservationCreationDto = new ReservationCreationDto("userName", 2, LocalDate.parse("2200-08-10"), LocalDate.parse("2200-08-12"));

        boolean result = validator.isValid(reservationCreationDto, constraintValidatorContext);

        assertTrue(result);
    }

    @Test
    public void startDateEqualToEndDateShouldNotBeValid() {
        ReservationCreationDto reservationCreationDto = new ReservationCreationDto("userName", 2, LocalDate.parse("2200-08-10"), LocalDate.parse("2200-08-10"));

        boolean result = validator.isValid(reservationCreationDto, constraintValidatorContext);

        assertFalse(result);
    }

    @Test
    public void startDateAfterEndDateShouldNotBeValid() {
        ReservationCreationDto reservationCreationDto = new ReservationCreationDto("userName", 2, LocalDate.parse("2200-08-18"), LocalDate.parse("2200-08-12"));

        boolean result = validator.isValid(reservationCreationDto, constraintValidatorContext);

        assertFalse(result);
    }

    @Test
    public void nullDatesShouldBeValid() {
        ReservationCreationDto reservationCreationDto = new ReservationCreationDto("userName", 2, null, null);

        boolean result = validator.isValid(reservationCreationDto, constraintValidatorContext);

        assertTrue(result);
    }

    @Test
    public void oneNullDateShouldNotBeValid() {
        ReservationCreationDto reservationCreationDto = new ReservationCreationDto("userName", 2, LocalDate.parse("2200-08-18"), null);

        boolean result = validator.isValid(reservationCreationDto, constraintValidatorContext);

        assertFalse(result);
    }

}