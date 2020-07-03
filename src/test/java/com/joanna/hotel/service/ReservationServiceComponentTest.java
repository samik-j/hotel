package com.joanna.hotel.service;

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
import java.util.ArrayList;
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
        ReservationDto reservationDto = new ReservationDto("some", 4, NOW.plusDays(2), NOW.plusDays(4));
        reservationService.save(reservationDto);

        List<ReservationDto> list = reservationService.findAll();

        assertThat(list).hasSize(1)
                        .contains(reservationDto);
    }

    @Test
    public void shouldNotSaveReservationIfNoRoomCanBeBooked() {
        Room room = new Room(RoomType.BASIC, 5);
        room = roomRepository.save(room);
        ReservationDto reservationDto = new ReservationDto("some", 3, NOW.plusDays(2), NOW.plusDays(4));
        reservationService.save(reservationDto);

        Throwable throwable = catchThrowable(() -> reservationService.save(reservationDto));

        assertThat(throwable).isInstanceOf(RuntimeException.class);
        assertThat(reservationService.findAll()).hasSize(1)
                                                .containsOnly(reservationDto);
        assertThat(roomRepository.findById(room.getId()).get().getReservations()).hasSize(1);
    }

    @Test
    public void shouldNotSaveReservationIfThereAreNoRooms() {
        ReservationDto reservationDto = new ReservationDto("some", 5, NOW.plusDays(2), NOW.plusDays(4));

        Throwable throwable = catchThrowable(() -> reservationService.save(reservationDto));

        assertThat(throwable).isInstanceOf(RuntimeException.class);
        assertThat(reservationService.findAll()).isEmpty();
    }

}