package com.joanna.hotel;

import com.joanna.hotel.dto.ReservationCreationDto;
import com.joanna.hotel.dto.ReservationDto;

import java.time.LocalDate;

public class TestUtils {

    public static String reservationCreationDtoJson() {
        return "{" +
                "\"userName\":\"someone\"," +
                "\"numberOfPeople\":3," +
                "\"startDate\":\"2200-07-08\"," +
                "\"endDate\":\"2200-08-08\"" +
                "}";
    }

    public static ReservationCreationDto reservationCreationDto() {
        return new ReservationCreationDto("someone", 3, LocalDate.parse("2200-07-08"), LocalDate.parse("2200-08-08"));
    }

    public static String reservationDtoJson() {
        return "{" +
                "\"id\":1," +
                "\"userName\":\"someone\"," +
                "\"numberOfPeople\":3," +
                "\"startDate\":\"2200-07-08\"," +
                "\"endDate\":\"2200-08-08\"" +
                "}";
    }

    public static ReservationDto reservationDto() {
        return new ReservationDto(1L, "someone", 3, LocalDate.parse("2200-07-08"), LocalDate.parse("2200-08-08"));
    }
}
