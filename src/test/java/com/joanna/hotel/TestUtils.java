package com.joanna.hotel;

import com.joanna.hotel.dto.ReservationDto;

import java.time.LocalDate;

public class TestUtils {

    public static String reservationDtoJson() {
        return "{" +
                "\"userName\":\"someone\"," +
                "\"numberOfPeople\":3," +
                "\"startDate\":\"2200-07-08\"," +
                "\"endDate\":\"2200-08-08\"" +
                "}";
    }

    public static ReservationDto reservationDto() {
        return new ReservationDto("someone", 3, LocalDate.parse("2200-07-08"), LocalDate.parse("2200-08-08"));
    }
}
