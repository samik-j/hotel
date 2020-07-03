package com.joanna.hotel.service;

import com.joanna.hotel.dto.ReservationCreationDto;
import com.joanna.hotel.dto.ReservationDto;
import com.joanna.hotel.model.Room;
import com.joanna.hotel.model.RoomType;
import com.joanna.hotel.repository.RoomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReservationServiceComponentTest {

    public static final LocalDate NOW = LocalDate.now();

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void shouldSaveReservation() {
        roomRepository.save(new Room(RoomType.BASIC, 5));
        ReservationCreationDto reservationCreationDto = new ReservationCreationDto("some", 4, NOW.plusDays(2), NOW.plusDays(4));
        reservationService.save(reservationCreationDto);

        List<ReservationDto> reservationDtos = reservationService.findAll();

        assertThat(reservationDtos).hasSize(1);
        assertThat(reservationDtos.get(0)).isEqualToIgnoringGivenFields(reservationCreationDto, "id");
    }

    @Test
    public void shouldSaveReservationOnARoomWithNonOverlappingReservation() {
        roomRepository.save(new Room(RoomType.BASIC, 5));
        ReservationCreationDto reservationCreationDto = new ReservationCreationDto("some", 4, NOW.plusDays(2), NOW.plusDays(4));
        reservationService.save(reservationCreationDto);

        ReservationCreationDto reservationCreationDto2 = new ReservationCreationDto("some", 4, NOW.plusDays(5), NOW.plusDays(7));
        reservationService.save(reservationCreationDto2);

        List<ReservationDto> reservationDtos = reservationService.findAll();
        assertThat(reservationDtos).hasSize(2);
        reservationDtos.sort(Comparator.comparingLong(ReservationDto::getId));
        assertThat(reservationDtos.get(0)).isEqualToIgnoringGivenFields(reservationCreationDto, "id")
                                          .hasFieldOrPropertyWithValue("id", 1L);
        assertThat(reservationDtos.get(1)).isEqualToIgnoringGivenFields(reservationCreationDto2, "id")
                                          .hasFieldOrPropertyWithValue("id", 2L);
    }

    @Test
    public void shouldNotSaveReservationIfNoRoomCanBeBooked() {
        Room room = new Room(RoomType.BASIC, 5);
        room = roomRepository.save(room);
        ReservationCreationDto reservationCreationDto = new ReservationCreationDto("some", 3, NOW.plusDays(2), NOW.plusDays(4));
        reservationService.save(reservationCreationDto);

        Throwable throwable = catchThrowable(() -> reservationService.save(reservationCreationDto));

        assertThat(throwable).isInstanceOf(RuntimeException.class);
        List<ReservationDto> reservationDtos = reservationService.findAll();
        assertThat(reservationDtos).hasSize(1);
        assertThat(reservationDtos.get(0)).isEqualToIgnoringGivenFields(reservationCreationDto, "id");
        assertThat(roomRepository.findById(room.getId()).get().getReservations()).hasSize(1);
    }

    @Test
    public void shouldNotSaveReservationIfThereAreNoRooms() {
        ReservationCreationDto reservationCreationDto = new ReservationCreationDto("some", 5, NOW.plusDays(2), NOW.plusDays(4));

        Throwable throwable = catchThrowable(() -> reservationService.save(reservationCreationDto));

        assertThat(throwable).isInstanceOf(RuntimeException.class);
        assertThat(reservationService.findAll()).isEmpty();
    }

}