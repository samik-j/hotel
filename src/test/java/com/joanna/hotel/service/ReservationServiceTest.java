package com.joanna.hotel.service;

import com.joanna.hotel.dto.ReservationCreationDto;
import com.joanna.hotel.dto.ReservationDto;
import com.joanna.hotel.exception.NoRoomsAvailableException;
import com.joanna.hotel.exception.ResourceNotFoundException;
import com.joanna.hotel.model.Reservation;
import com.joanna.hotel.model.Room;
import com.joanna.hotel.model.RoomType;
import com.joanna.hotel.repository.ReservationRepository;
import com.joanna.hotel.repository.RoomRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.joanna.hotel.TestUtils.reservationCreationDto;
import static com.joanna.hotel.TestUtils.reservationDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    public static final LocalDate NOW = LocalDate.now();
    public static final Room ROOM = new Room(RoomType.BASIC, 1);
    public static final Reservation RESERVATION = new Reservation(1L, "someone", 3, LocalDate.parse("2200-07-08"), LocalDate.parse("2200-08-08"), ROOM);

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Before
    public void setUp() {
        when(roomRepository.findByRoomType(RoomType.BASIC)).thenReturn(Arrays.asList(ROOM));
        when(roomRepository.save(any())).thenReturn(ROOM);
        when(reservationRepository.save(any())).thenReturn(RESERVATION);
    }

    @Test
    public void shouldSaveReservation() {
        ReservationCreationDto reservationCreationDto = reservationCreationDto();

        reservationService.save(reservationCreationDto);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    public void shouldNotSaveReservationAndThrowExceptionIfThereAreNoRooms() {
        when(roomRepository.findByRoomType(RoomType.BASIC)).thenReturn(new ArrayList<>());
        ReservationCreationDto reservationCreationDto = reservationCreationDto();

        Throwable throwable = catchThrowable(() -> reservationService.save(reservationCreationDto));

        assertThat(throwable).isInstanceOf(NoRoomsAvailableException.class);
        verifyNoInteractions(reservationRepository);
        verify(roomRepository, times(0)).save(any(Room.class));
    }

    @Test
    public void shouldNotSaveReservationAndThrowExceptionIfThereAreNoRoomsWithRightCapacity() {
        ReservationCreationDto reservationCreationDto = new ReservationCreationDto("some", 20, NOW.plusDays(2), NOW.plusDays(4));

        Throwable throwable = catchThrowable(() -> reservationService.save(reservationCreationDto));

        assertThat(throwable).isInstanceOf(NoRoomsAvailableException.class);
        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(roomRepository);
    }

    @Test
    public void shouldFindAllReservations() {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(RESERVATION));

        List<ReservationDto> reservationDtos = reservationService.findAll();

        assertThat(reservationDtos).hasSize(1);
        assertThat(reservationDtos.get(0)).isEqualToComparingFieldByField(reservationDto());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    public void shouldFindReservationById() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(RESERVATION));

        ReservationDto reservationDto = reservationService.findById(1L);

        assertThat(reservationDto).isEqualToComparingFieldByField(reservationDto());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    public void shouldFindReservationsByRoomNumber() {
        when(reservationRepository.findByRoomNumber(1)).thenReturn(Arrays.asList(RESERVATION));

        List<ReservationDto> reservationDtos = reservationService.findByRoomNumber(1);

        assertThat(reservationDtos).hasSize(1);
        assertThat(reservationDtos.get(0)).isEqualToComparingFieldByField(reservationDto());
        verify(reservationRepository, times(1)).findByRoomNumber(1);
    }

    @Test
    public void shouldReturnEmptyListIfThereAreNoReservationsByRoomNumber() {
        when(reservationRepository.findByRoomNumber(1)).thenReturn(new ArrayList<>());

        List<ReservationDto> reservationDtos = reservationService.findByRoomNumber(1);

        assertThat(reservationDtos).isEmpty();
        verify(reservationRepository, times(1)).findByRoomNumber(1);
    }

    @Test
    public void shouldThrowExceptionOnFindReservationByNonExistingId() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> reservationService.findById(1L));

        assertThat(throwable).isExactlyInstanceOf(ResourceNotFoundException.class);
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    public void shouldDeleteById() {
        reservationService.delete(1L);
    }

}