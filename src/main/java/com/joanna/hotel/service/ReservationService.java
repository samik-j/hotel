package com.joanna.hotel.service;

import com.joanna.hotel.dto.ReservationCreationDto;
import com.joanna.hotel.dto.ReservationDto;
import com.joanna.hotel.exception.NoRoomsAvailableException;
import com.joanna.hotel.exception.ResourceNotFoundException;
import com.joanna.hotel.mapper.ReservationMapper;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    public ReservationDto findById(Long reservationId) {
        return ReservationMapper.INSTANCE.reservationToReservationDto(reservationRepository.findById(reservationId)
                                                                                           .orElseThrow(ResourceNotFoundException::new));
    }

    public List<ReservationDto> findAll() {
        return reservationRepository.findAll().stream()
                                    .map(ReservationMapper.INSTANCE::reservationToReservationDto).collect(Collectors.toList());
    }

    public List<ReservationDto> findByRoomNumber(Integer roomNumber) {
        return reservationRepository.findByRoomNumber(roomNumber)
                                    .stream()
                                    .map(ReservationMapper.INSTANCE::reservationToReservationDto)
                                    .collect(Collectors.toList());
    }

    @Transactional
    public Long save(ReservationCreationDto reservationCreationDto) {
        Room roomToBook = findRoom(reservationCreationDto);

        Reservation reservation = ReservationMapper.INSTANCE.reservationCreationDtoAndRoomToReservation(reservationCreationDto, roomToBook);

        roomToBook.getReservations().add(reservation);
        reservation = reservationRepository.save(reservation);
        roomRepository.save(roomToBook);

        return reservation.getId();
    }

    @Transactional
    public ReservationDto updateReservation(Long reservationId, ReservationCreationDto reservationCreationDto) {
        Optional<Reservation> byId = reservationRepository.findById(reservationId);
        Reservation reservation = byId.orElseThrow(ResourceNotFoundException::new);
        reservation.setUserName(reservationCreationDto.getUserName());
        reservation.setStartDate(reservationCreationDto.getStartDate());
        reservation.setEndDate(reservationCreationDto.getEndDate());
        if (!reservation.getNumberOfPeople().equals(reservationCreationDto.getNumberOfPeople())) {
            reservation.setNumberOfPeople(reservationCreationDto.getNumberOfPeople());
            Room room = findRoom(reservationCreationDto);
            reservation.setRoom(room);
            roomRepository.save(room);
        }

        reservation = reservationRepository.save(reservation);

        return ReservationMapper.INSTANCE.reservationToReservationDto(reservation);
    }

    public void delete(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    private Room findRoom(ReservationCreationDto reservationCreationDto) {
        List<Room> rooms = roomRepository.findByRoomType(checkRoomType(reservationCreationDto.getNumberOfPeople()));

        return rooms.stream()
                    .filter(room -> canHandleReservation(room, reservationCreationDto))
                    .findFirst()
                    .orElseThrow(NoRoomsAvailableException::new);
    }

    private RoomType checkRoomType(Integer numberOfPeople) {
        return RoomType.sortedByCapacity.stream()
                                        .filter(roomType -> numberOfPeople <= roomType.capacity)
                                        .findFirst()
                                        .orElseThrow(NoRoomsAvailableException::new);
    }

    private boolean canHandleReservation(Room room, ReservationCreationDto reservationCreationDto) {
        return room.getReservations().stream()
                   .allMatch(existingReservation -> datesDoNotNotOverlap(existingReservation.getStartDate(),
                                                                         existingReservation.getEndDate(),
                                                                         reservationCreationDto.getStartDate(),
                                                                         reservationCreationDto.getEndDate()));
    }

    private boolean datesDoNotNotOverlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        return (startDate1.isBefore(startDate2) && endDate1.isBefore(startDate2)) || (startDate1.isAfter(endDate2) && endDate1.isAfter(endDate2));
    }
}
