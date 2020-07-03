package com.joanna.hotel.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static com.joanna.hotel.TestUtils.reservationCreationDto;
import static com.joanna.hotel.TestUtils.reservationCreationDtoJson;
import static org.assertj.core.api.Assertions.assertThat;


public class ReservationCreationDtoTest {

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void shouldSerialize() throws JsonProcessingException {
        assertThat(reservationCreationDtoJson()).isEqualTo(objectMapper.writeValueAsString(reservationCreationDto()));
    }

    @Test
    public void shouldDeserialize() throws JsonProcessingException {
        assertThat(reservationCreationDto()).isEqualTo(objectMapper.readValue(reservationCreationDtoJson(), ReservationCreationDto.class));
    }

    @Test
    public void shouldThrowValidationErrorsOnMissingMandatoryFields() {
        ReservationCreationDto reservationCreationDto = new ReservationCreationDto();

        Set<ConstraintViolation<ReservationCreationDto>> violations = validator.validate(reservationCreationDto);

        assertThat(violations).hasSize(4);
    }

    @Test
    public void shouldThrowValidationErrorsOnInvalidDates() {
        ReservationCreationDto reservationCreationDto = new ReservationCreationDto("someone", 4, LocalDate.parse("2020-07-20"), LocalDate.parse("2020-06-20"));

        Set<ConstraintViolation<ReservationCreationDto>> violations = validator.validate(reservationCreationDto);

        assertThat(violations).hasSize(1).extracting("message").containsExactly("start date must be before end date");
    }

}