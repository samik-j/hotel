package com.joanna.hotel.controller;

import com.joanna.hotel.model.Room;
import com.joanna.hotel.model.RoomType;
import com.joanna.hotel.repository.ReservationRepository;
import com.joanna.hotel.repository.RoomRepository;
import org.junit.After;
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

import static com.joanna.hotel.TestUtils.reservationCreationDtoJson;
import static com.joanna.hotel.TestUtils.reservationDtoJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationControllerComponentTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnEmptyListForGetReservations() throws Exception {
        ResultActions result = mockMvc.perform(get("/reservations"));

        assertSuccessWithResponse(result, "[]");
    }

    @Test
    public void shouldReturnExistingReservationsForGetReservations() throws Exception {
        givenReservationExists();

        ResultActions result = mockMvc.perform(get("/reservations"));

        assertSuccessWithResponse(result, "[" + reservationDtoJson() + "]");
    }

    @Test
    public void shouldGetReservationById() throws Exception {
        givenReservationExists();

        ResultActions result = mockMvc.perform(get("/reservations/{id}", 1));

        assertSuccessWithResponse(result, reservationDtoJson());
    }

    @Test
    public void shouldReturn404OnGetReservationByNonExistingId() throws Exception {
        ResultActions result = mockMvc.perform(get("/reservations/{id}", 1));

        result.andExpect(status().isNotFound());
    }


    @Test
    public void shouldReturnEmptyListForGetReservationsByRoomNumber() throws Exception {
        givenReservationExists();

        ResultActions result = mockMvc.perform(get("/reservations?roomNumber=1"));

        assertSuccessWithResponse(result, "[]");
    }

    @Test
    public void shouldReturnExistingReservationsForGetReservationsByRoomNumber() throws Exception {
        givenReservationExists();

        ResultActions result = mockMvc.perform(get("/reservations?roomNumber=5"));

        assertSuccessWithResponse(result, "[" + reservationDtoJson() + "]");
    }

    @Test
    public void shouldCreateReservation() throws Exception {
        roomRepository.save(new Room(RoomType.BASIC, 5));
        ResultActions result = mockMvc.perform(post("/reservations")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(reservationCreationDtoJson()));

        result.andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnBadRequestIfNotPossibleToBookOnCreation() throws Exception {
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
    public void shouldReturnOkWhenDeletingNonExistingResource() throws Exception {
        ResultActions result = mockMvc.perform(delete("/reservations/{id}", 1));

        result.andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteResource() throws Exception {
        givenReservationExists();

        ResultActions result = mockMvc.perform(delete("/reservations/{id}", 1));

        result.andExpect(status().isOk());
        mockMvc.perform(get("/reservations/{id}", 1)).andExpect(status().isNotFound());
    }

    private void assertSuccessWithResponse(ResultActions resultActions, String response) throws Exception {
        resultActions.andExpect(status().isOk())
                     .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        assertThat(resultActions.andReturn().getResponse().getContentAsString()).isEqualTo(response);
    }

    private void givenReservationExists() throws Exception {
        roomRepository.save(new Room(RoomType.BASIC, 5));
        mockMvc.perform(post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(reservationCreationDtoJson()));
    }
}