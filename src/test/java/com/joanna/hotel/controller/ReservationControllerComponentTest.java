package com.joanna.hotel.controller;

import com.joanna.hotel.dto.ReservationDto;
import com.joanna.hotel.model.Room;
import com.joanna.hotel.model.RoomType;
import com.joanna.hotel.repository.ReservationRepository;
import com.joanna.hotel.repository.RoomRepository;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static com.joanna.hotel.TestUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ReservationControllerComponentTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MockMvc mockMvc;

    @After
    public void cleanup() {
        reservationRepository.deleteAll();
    }

    @Test
    public void shouldGetCreatedReservationById() throws Exception {
        Long id = givenReservationExists();

        ResultActions result = mockMvc.perform(get("/reservations/{id}", id));

        assertSuccessWithResponse(result, reservationDtoWithId(id));
    }

    @Test
    public void shouldReturnCreatedReservationsForGetReservationsByRoomNumber() throws Exception {
        Long id1 = givenReservationExists();
        Long id2 = givenReservationExists();

        ResultActions result = mockMvc.perform(get("/reservations/findByRoomNumber?roomNumber=1"));

        assertSuccessWithListResponse(result, Arrays.asList(reservationDtoWithId(id1), reservationDtoWithId(id2)));
    }

    @Test
    public void shouldReturnCreatedReservationsForGetReservations() throws Exception {
        Long id1 = givenReservationExists();
        Long id2 = givenReservationExists();

        ResultActions result = mockMvc.perform(get("/reservations"));

        assertSuccessWithListResponse(result, Arrays.asList(reservationDtoWithId(id1), reservationDtoWithId(id2)));
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
    public void shouldUpdateCreatedReservation() throws Exception {
        Long id = givenReservationExists();
        JSONObject jsonObject = new JSONObject(reservationCreationDtoJson());
        jsonObject.put("userName", "someoneElse");

        ResultActions result = mockMvc.perform(put("/reservations/{id}", id)
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(jsonObject.toString()));


        ReservationDto reservationDto = reservationDtoWithId(id);
        reservationDto.setUserName("someoneElse");
        assertSuccessWithResponse(result, reservationDto);
    }

    @Test
    public void shouldUpdateCreatedReservationToLargerRoom() throws Exception {
        roomRepository.save(new Room(RoomType.SUITE, 5));

        Long id = givenReservationExists();
        JSONObject jsonObject = new JSONObject(reservationCreationDtoJson());
        jsonObject.put("numberOfPeople", "5");

        ResultActions result = mockMvc.perform(put("/reservations/{id}", id)
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(jsonObject.toString()));


        ReservationDto reservationDto = reservationDtoWithId(id);
        reservationDto.setNumberOfPeople(5);
        reservationDto.setRoomNumber(5);
        assertSuccessWithResponse(result, reservationDto);
    }

    @Test
    public void shouldReturnOkWhenDeletingNonExistingReservation() throws Exception {
        ResultActions result = mockMvc.perform(delete("/reservations/{id}", 1238));

        result.andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteCreatedReservation() throws Exception {
        Long id = givenReservationExists();

        ResultActions result = mockMvc.perform(delete("/reservations/{id}", id));

        result.andExpect(status().isOk());
        mockMvc.perform(get("/reservations/{id}", 1)).andExpect(status().isNotFound());
    }

    private Long givenReservationExists() throws Exception {
        roomRepository.save(new Room(RoomType.BASIC, 1));
        ResultActions resultActions = mockMvc.perform(post("/reservations")
                                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                              .content(reservationCreationDtoJson()));

        return new JSONObject(resultActions.andReturn().getResponse().getContentAsString()).getLong("id");
    }
}
