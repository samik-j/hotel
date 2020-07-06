package com.joanna.hotel.controller;

import com.joanna.hotel.dto.ReservationDto;
import com.joanna.hotel.model.Room;
import com.joanna.hotel.model.RoomType;
import com.joanna.hotel.repository.RoomRepository;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.joanna.hotel.TestUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationControllerComponentTest {

    public static final int RESERVATION_ROOM_NUMBER = 1;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnEmptyListForGetReservations() throws Exception {
        ResultActions result = mockMvc.perform(get("/reservations"));

        assertSuccessWithListResponse(result, new ArrayList<>());
    }

    @Test
    public void shouldReturnExistingReservationsForGetReservations() throws Exception {
        givenReservationExists();

        ResultActions result = mockMvc.perform(get("/reservations"));

        assertSuccessWithListResponse(result, Arrays.asList(reservationDto()));
    }

    @Test
    public void shouldGetReservationById() throws Exception {
        givenReservationExists();

        ResultActions result = mockMvc.perform(get("/reservations/{id}", RESERVATION_ROOM_NUMBER));

        assertSuccessWithResponse(result, reservationDto());
    }

    @Test
    public void shouldReturn404OnGetReservationByNonExistingId() throws Exception {
        ResultActions result = mockMvc.perform(get("/reservations/{id}", 1));

        result.andExpect(status().isNotFound());
    }


    @Test
    public void shouldReturnEmptyListForGetReservationsByRoomNumber() throws Exception {
        givenReservationExists();

        ResultActions result = mockMvc.perform(get("/reservations?roomNumber=2"));

        assertSuccessWithListResponse(result, new ArrayList<>());
    }

    @Test
    public void shouldReturnExistingReservationsForGetReservationsByRoomNumber() throws Exception {
        givenReservationExists();

        ResultActions result = mockMvc.perform(get("/reservations?roomNumber=1"));

        assertSuccessWithListResponse(result, Arrays.asList(reservationDto()));
    }

    @Test
    public void shouldCreateReservation() throws Exception {
        roomRepository.save(new Room(RoomType.BASIC, 1));
        ResultActions result = mockMvc.perform(post("/reservations")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(reservationCreationDtoJson()));

        result.andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnBadRequestIfNoRoomsAvailableOnCreation() throws Exception {
        ResultActions result = mockMvc.perform(post("/reservations")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(reservationCreationDtoJson()));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestForInvalidRequestBodyOnCreation() throws Exception {
        ResultActions result = mockMvc.perform(post("/reservations")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content("{}"));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateReservation() throws Exception {
        givenReservationExists();
        JSONObject jsonObject = new JSONObject(reservationCreationDtoJson());
        jsonObject.put("userName", "someoneElse");

        ResultActions result = mockMvc.perform(put("/reservations/1")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(jsonObject.toString()));


        ReservationDto reservationDto = reservationDto();
        reservationDto.setUserName("someoneElse");
        assertSuccessWithResponse(result, reservationDto);
    }

    @Test
    public void shouldReturnNotFoundOnUpdateIfReservationDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(put("/reservations/1")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(reservationCreationDtoJson()));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBadRequestForInvalidRequestBodyOnUpdate() throws Exception {
        ResultActions result = mockMvc.perform(put("/reservations/1")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content("{}"));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestForNoRoomsAvailableOnUpdate() throws Exception {
        givenReservationExists();
        JSONObject jsonObject = new JSONObject(reservationCreationDtoJson());
        jsonObject.put("numberOfPeople", "6");

        ResultActions result = mockMvc.perform(put("/reservations/1")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(jsonObject.toString()));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnOkWhenDeletingNonExistingResource() throws Exception {
        ResultActions result = mockMvc.perform(delete("/reservations/{id}", 1));

        result.andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteReservation() throws Exception {
        givenReservationExists();

        ResultActions result = mockMvc.perform(delete("/reservations/{id}", 1));

        result.andExpect(status().isOk());
        mockMvc.perform(get("/reservations/{id}", 1)).andExpect(status().isNotFound());
    }

    private void assertSuccessWithListResponse(ResultActions resultActions, List<ReservationDto> reservationDtos) throws Exception {
        resultActions.andExpect(status().isOk())
                     .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                     .andExpect(jsonPath("$.length()").value(reservationDtos.size()));
        for (int i = 0; i < reservationDtos.size(); i++) {
            resultActions.andExpect(jsonPath("[" + i + "].id").value(reservationDtos.get(0).getId()))
                         .andExpect(jsonPath("[" + i + "].userName").value(reservationDtos.get(0).getUserName()))
                         .andExpect(jsonPath("[" + i + "].numberOfPeople").value(reservationDtos.get(0).getNumberOfPeople()))
                         .andExpect(jsonPath("[" + i + "].startDate").value(reservationDtos.get(0).getStartDate().toString()))
                         .andExpect(jsonPath("[" + i + "].endDate").value(reservationDtos.get(0).getEndDate().toString()))
                         .andExpect(jsonPath("[" + i + "].roomNumber").value(reservationDtos.get(0).getRoomNumber()));
        }
    }

    private void assertSuccessWithResponse(ResultActions resultActions, ReservationDto reservationDto) throws Exception {
        resultActions.andExpect(status().isOk())
                     .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                     .andExpect(jsonPath("$.id").value(reservationDto.getId()))
                     .andExpect(jsonPath("$.userName").value(reservationDto.getUserName()))
                     .andExpect(jsonPath("$.numberOfPeople").value(reservationDto.getNumberOfPeople()))
                     .andExpect(jsonPath("$.startDate").value(reservationDto.getStartDate().toString()))
                     .andExpect(jsonPath("$.endDate").value(reservationDto.getEndDate().toString()))
                     .andExpect(jsonPath("$.roomNumber").value(reservationDto.getRoomNumber()));
    }

    private void givenReservationExists() throws Exception {
        roomRepository.save(new Room(RoomType.BASIC, 1));
        mockMvc.perform(post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(reservationCreationDtoJson()));
    }
}