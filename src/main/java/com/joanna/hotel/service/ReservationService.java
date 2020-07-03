package com.joanna.hotel.service;

import com.joanna.hotel.dto.ReservationDto;
import com.joanna.hotel.exception.NoRoomsAvailableException;
import com.joanna.hotel.model.Reservation;
import com.joanna.hotel.model.Room;
import com.joanna.hotel.model.RoomType;
import com.joanna.hotel.repository.ReservationRepository;
import com.joanna.hotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public void save(ReservationDto reservationDto) {
        List<Room> rooms = roomRepository.findByRoomType(checkRoomType(reservationDto.getNumberOfPeople()));

        Room roomToBook = rooms.stream()
                               .filter(room -> canHandleReservation(room, reservationDto))
                               .findFirst()
                               .orElseThrow(NoRoomsAvailableException::new);

        Reservation reservation = new Reservation(reservationDto.getUserName(),
                                                  reservationDto.getNumberOfPeople(),
                                                  reservationDto.getStartDate(),
                                                  reservationDto.getEndDate(),
                                                  roomToBook);

        roomToBook.getReservations().add(reservation);
        reservationRepository.save(reservation);
        roomRepository.save(roomToBook);
    }

    public List<ReservationDto> findAll() {
        return reservationRepository.findAll().stream()
                                    .map(reservation -> new ReservationDto(reservation.getUserName(),
                                                                           reservation.getNumberOfPeople(),
                                                                           reservation.getStartDate(),
                                                                           reservation.getEndDate())).collect(Collectors.toList());
    }

    private RoomType checkRoomType(Integer numberOfPeople) {
        return RoomType.sortedByCapacity.stream()
                                        .filter(roomType -> numberOfPeople <= roomType.capacity)
                                        .findFirst()
                                        .orElseThrow(NoRoomsAvailableException::new);
    }

    private boolean canHandleReservation(Room room, ReservationDto reservationDto) {
        return room.getReservations().stream()
                   .allMatch(existingReservation -> datesDoNotNotOverlap(existingReservation.getStartDate(),
                                                                         existingReservation.getEndDate(),
                                                                         reservationDto.getStartDate(),
                                                                         reservationDto.getEndDate()));
    }

    private boolean datesDoNotNotOverlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        return (startDate1.isBefore(startDate2) && endDate1.isBefore(startDate2)) || (startDate1.isAfter(endDate2) && endDate1.isAfter(endDate2));
    }
}
