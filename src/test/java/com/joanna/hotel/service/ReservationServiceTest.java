package com.joanna.hotel.service;

import com.joanna.hotel.dto.ReservationDto;
import com.joanna.hotel.exception.NoRoomsAvailableException;
import com.joanna.hotel.model.Reservation;
import com.joanna.hotel.model.Room;
import com.joanna.hotel.model.RoomType;
import com.joanna.hotel.repository.ReservationRepository;
import com.joanna.hotel.repository.RoomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    public static final LocalDate NOW = LocalDate.now();

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository1;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    public void shouldSaveReservation() {
        Room room = new Room(RoomType.BASIC, 1, new ArrayList<>());
        when(roomRepository1.findAll()).thenReturn(Arrays.asList(room));
        when(roomRepository1.save(any(Room.class))).thenReturn(room);
        ReservationDto reservationDto = new ReservationDto("some", 5, NOW.plusDays(2), NOW.plusDays(4));
        reservationService.save(reservationDto);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(roomRepository1, times(1)).save(any(Room.class));
    }

    @Test
    public void shouldNotSaveReservationIfThereAreNoRooms() {
        when(roomRepository1.findAll()).thenReturn(new ArrayList<>());
        ReservationDto reservationDto = new ReservationDto("some", 5, NOW.plusDays(2), NOW.plusDays(4));

        Throwable throwable = catchThrowable(() -> reservationService.save(reservationDto));

        assertThat(throwable).isInstanceOf(NoRoomsAvailableException.class);
        verify(reservationRepository, times(0)).save(any(Reservation.class));
        verify(roomRepository1, times(0)).save(any(Room.class));
    }

    @Test
    public void shouldNotSaveReservationIfThereAreNoRoomsWithRightCapacity() {
        Room room = new Room(RoomType.BASIC, 1, new ArrayList<>());
        when(roomRepository1.findAll()).thenReturn(Arrays.asList(room));
        ReservationDto reservationDto = new ReservationDto("some", 5, NOW.plusDays(2), NOW.plusDays(4));

        Throwable throwable = catchThrowable(() -> reservationService.save(reservationDto));

        assertThat(throwable).isInstanceOf(NoRoomsAvailableException.class);
        verify(reservationRepository, times(0)).save(any(Reservation.class));
        verify(roomRepository1, times(0)).save(any(Room.class));
    }


}