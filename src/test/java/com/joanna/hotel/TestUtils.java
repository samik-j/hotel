package com.joanna.hotel;

import com.joanna.hotel.dto.ReservationCreationDto;
import com.joanna.hotel.dto.ReservationDto;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
                "\"endDate\":\"2200-08-08\"," +
                "\"roomNumber\":1" +
                "}";
    }

    public static ReservationDto reservationDto() {
        return new ReservationDto(1L, "someone", 3, LocalDate.parse("2200-07-08"), LocalDate.parse("2200-08-08"), 1);
    }

    public static ReservationDto reservationDtoWithId(Long id) {
        return new ReservationDto(id, "someone", 3, LocalDate.parse("2200-07-08"), LocalDate.parse("2200-08-08"), 1);
    }


    public static void assertSuccessWithListResponse(ResultActions resultActions, List<ReservationDto> reservationDtos) throws Exception {
        resultActions.andExpect(status().isOk())
                     .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                     .andExpect(jsonPath("$.length()").value(reservationDtos.size()));
        for (int i = 0; i < reservationDtos.size(); i++) {
            resultActions.andExpect(jsonPath("[" + i + "].id").value(reservationDtos.get(i).getId()))
                         .andExpect(jsonPath("[" + i + "].userName").value(reservationDtos.get(i).getUserName()))
                         .andExpect(jsonPath("[" + i + "].numberOfPeople").value(reservationDtos.get(i).getNumberOfPeople()))
                         .andExpect(jsonPath("[" + i + "].startDate").value(reservationDtos.get(i).getStartDate().toString()))
                         .andExpect(jsonPath("[" + i + "].endDate").value(reservationDtos.get(i).getEndDate().toString()))
                         .andExpect(jsonPath("[" + i + "].roomNumber").value(reservationDtos.get(i).getRoomNumber()));
        }
    }

    public static void assertSuccessWithResponse(ResultActions resultActions, ReservationDto reservationDto) throws Exception {
        resultActions.andExpect(status().isOk())
                     .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                     .andExpect(jsonPath("$.id").value(reservationDto.getId()))
                     .andExpect(jsonPath("$.userName").value(reservationDto.getUserName()))
                     .andExpect(jsonPath("$.numberOfPeople").value(reservationDto.getNumberOfPeople()))
                     .andExpect(jsonPath("$.startDate").value(reservationDto.getStartDate().toString()))
                     .andExpect(jsonPath("$.endDate").value(reservationDto.getEndDate().toString()))
                     .andExpect(jsonPath("$.roomNumber").value(reservationDto.getRoomNumber()));
    }
}
