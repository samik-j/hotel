package com.joanna.hotel.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joanna.hotel.dto.ReservationDto;
import com.joanna.hotel.model.Room;
import com.joanna.hotel.model.RoomType;
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

import java.util.List;

import static com.joanna.hotel.TestUtils.reservationDto;
import static com.joanna.hotel.TestUtils.reservationDtoJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationControllerComponentTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnEmptyListForGetReservations() throws Exception {
        ResultActions result = mockMvc.perform(get("/reservations"));

        result.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

    }

    @Test
    public void shouldReturnExistingReservationsForGetReservations() throws Exception {
        roomRepository.save(new Room(RoomType.BASIC, 5));
        mockMvc.perform(post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(reservationDtoJson()));

        ResultActions result = mockMvc.perform(get("/reservations"));

        result.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        List<ReservationDto> reservationDtos = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(),
                                                                      new TypeReference<List<ReservationDto>>() {});
        assertThat(reservationDtos).hasSize(1);
        assertThat(reservationDtos.get(0)).isEqualToComparingFieldByField(reservationDto());
    }

    @Test
    public void shouldCreateReservation() throws Exception {
        roomRepository.save(new Room(RoomType.BASIC, 5));
        ResultActions result = mockMvc.perform(post("/reservations")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(reservationDtoJson()));

        result.andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnBadRequestIfNotPossibleToBook() throws Exception {
        ResultActions result = mockMvc.perform(post("/reservations")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(reservationDtoJson()));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestForInvalidRequestBody() throws Exception {
        ResultActions result = mockMvc.perform(post("/reservations")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content("{}"));

        result.andExpect(status().isBadRequest());
    }

}