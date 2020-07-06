package com.joanna.hotel.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static com.joanna.hotel.TestUtils.reservationDto;
import static com.joanna.hotel.TestUtils.reservationDtoJson;
import static org.assertj.core.api.Assertions.assertThat;

public class ReservationDtoTest {


    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void shouldSerialize() throws JsonProcessingException {
        assertThat(reservationDtoJson()).isEqualTo(objectMapper.writeValueAsString(reservationDto()));
    }

    @Test
    public void shouldDeserialize() throws JsonProcessingException {
        assertThat(reservationDto()).isEqualTo(objectMapper.readValue(reservationDtoJson(), ReservationDto.class));
    }

    @Test
    public void shouldThrowValidationErrorsOnMissingMandatoryFields() {
        ReservationDto reservationDto = new ReservationDto();

        Set<ConstraintViolation<ReservationDto>> violations = validator.validate(reservationDto);

        assertThat(violations).hasSize(6);
    }

}