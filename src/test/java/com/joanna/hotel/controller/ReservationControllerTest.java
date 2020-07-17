package com.joanna.hotel.controller;

import com.joanna.hotel.dto.ReservationDto;
import com.joanna.hotel.exception.NoRoomsAvailableException;
import com.joanna.hotel.exception.ResourceNotFoundException;
import com.joanna.hotel.service.ReservationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.joanna.hotel.TestUtils.reservationCreationDtoJson;
import static com.joanna.hotel.TestUtils.reservationDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureWebClient
@WebMvcTest(controllers = ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Test
    public void shouldReturnEmptyListForGetReservations() throws Exception {
        when(reservationService.findAll()).thenReturn(new ArrayList<>());

        ResultActions result = mockMvc.perform(get("/reservations"));

        assertSuccessWithListResponse(result, new ArrayList<>());
    }

    @Test
    public void shouldReturnExistingReservationsForGetReservations() throws Exception {
        when(reservationService.findAll()).thenReturn(Arrays.asList(reservationDto()));

        ResultActions result = mockMvc.perform(get("/reservations"));

        assertSuccessWithListResponse(result, Arrays.asList(reservationDto()));
    }

    @Test
    public void shouldGetReservationById() throws Exception {
        when(reservationService.findById(1L)).thenReturn(reservationDto());

        ResultActions result = mockMvc.perform(get("/reservations/{id}", 1));

        assertSuccessWithResponse(result, reservationDto());
    }

    @Test
    public void shouldReturnNotFoundOnGetReservationByNonExistingId() throws Exception {
        when(reservationService.findById(any())).thenThrow(new ResourceNotFoundException());
        ResultActions result = mockMvc.perform(get("/reservations/{id}", 1));

        result.andExpect(status().isNotFound());
        assertThat(result.andReturn().getResponse().getContentAsString()).contains("Resource not found");
    }

    @Test
    public void shouldReturnEmptyListForGetReservationsByRoomNumber() throws Exception {
        when(reservationService.findByRoomNumber(2)).thenReturn(new ArrayList<>());

        ResultActions result = mockMvc.perform(get("/reservations/findByRoomNumber?roomNumber=2"));

        assertSuccessWithListResponse(result, new ArrayList<>());
    }

    @Test
    public void shouldReturnExistingReservationsForGetReservationsByRoomNumber() throws Exception {
        when(reservationService.findByRoomNumber(1)).thenReturn(Arrays.asList(reservationDto(), reservationDto()));

        ResultActions result = mockMvc.perform(get("/reservations/findByRoomNumber?roomNumber=1"));

        assertSuccessWithListResponse(result, Arrays.asList(reservationDto(), reservationDto()));
    }

    @Test
    public void shouldReturnCreatedAndCreateReservation() throws Exception {
        when(reservationService.save(any())).thenReturn(1L);

        ResultActions result = mockMvc.perform(post("/reservations")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(reservationCreationDtoJson()));

        result.andExpect(status().isCreated())
              .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void shouldReturnBadRequestIfNoRoomsAvailableOnCreation() throws Exception {
        when(reservationService.save(any())).thenThrow(new NoRoomsAvailableException());

        ResultActions result = mockMvc.perform(post("/reservations")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(reservationCreationDtoJson()));

        result.andExpect(status().isBadRequest());
        assertThat(result.andReturn().getResponse().getContentAsString()).contains("No rooms available to book");
    }

    @Test
    public void shouldReturnBadRequestForInvalidRequestBodyOnCreation() throws Exception {
        ResultActions result = mockMvc.perform(post("/reservations")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content("{}"));

        result.andExpect(status().isBadRequest());
        assertThat(result.andReturn().getResponse().getContentAsString()).contains("Validation failed");
    }

    @Test
    public void shouldUpdateReservation() throws Exception {
        when(reservationService.updateReservation(eq(1L), any())).thenReturn(reservationDto());

        ResultActions result = mockMvc.perform(put("/reservations/1")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(reservationCreationDtoJson()));

        assertSuccessWithResponse(result, reservationDto());
    }

    @Test
    public void shouldReturnNotFoundOnUpdateIfReservationDoesNotExist() throws Exception {
        when(reservationService.updateReservation(eq(1L), any())).thenThrow(new ResourceNotFoundException());

        ResultActions result = mockMvc.perform(put("/reservations/1")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(reservationCreationDtoJson()));

        result.andExpect(status().isNotFound());
        assertThat(result.andReturn().getResponse().getContentAsString()).contains("Resource not found");
    }

    @Test
    public void shouldReturnBadRequestForInvalidRequestBodyOnUpdate() throws Exception {
        ResultActions result = mockMvc.perform(put("/reservations/1")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content("{}"));

        result.andExpect(status().isBadRequest());
        assertThat(result.andReturn().getResponse().getContentAsString()).contains("Validation failed");
    }

    @Test
    public void shouldReturnBadRequestForNoRoomsAvailableOnUpdate() throws Exception {
        when(reservationService.updateReservation(eq(1L), any())).thenThrow(new NoRoomsAvailableException());

        ResultActions result = mockMvc.perform(put("/reservations/1")
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(reservationCreationDtoJson()));

        result.andExpect(status().isBadRequest());
        assertThat(result.andReturn().getResponse().getContentAsString()).contains("No rooms available to book");
    }

    @Test
    public void shouldReturnOkWhenDeletingNonExistingResource() throws Exception {
        ResultActions result = mockMvc.perform(delete("/reservations/{id}", 1));

        result.andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteReservation() throws Exception {
        ResultActions result = mockMvc.perform(delete("/reservations/{id}", 1));

        result.andExpect(status().isOk());
    }

    private void assertSuccessWithListResponse(ResultActions resultActions, List<ReservationDto> reservationDtos) throws Exception {
        resultActions.andExpect(status().isOk())
                     .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                     .andExpect(jsonPath("$.length()").value(reservationDtos.size()));
        String s = resultActions.andReturn().getResponse().getContentAsString();
        for (int i = 0; i < reservationDtos.size(); i++) {
            resultActions.andExpect(jsonPath("[" + i + "].id").value(reservationDtos.get(i).getId()))
                         .andExpect(jsonPath("[" + i + "].userName").value(reservationDtos.get(i).getUserName()))
                         .andExpect(jsonPath("[" + i + "].numberOfPeople").value(reservationDtos.get(i).getNumberOfPeople()))
                         .andExpect(jsonPath("[" + i + "].startDate").value(reservationDtos.get(i).getStartDate().toString()))
                         .andExpect(jsonPath("[" + i + "].endDate").value(reservationDtos.get(i).getEndDate().toString()))
                         .andExpect(jsonPath("[" + i + "].roomNumber").value(reservationDtos.get(i).getRoomNumber()));
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
}