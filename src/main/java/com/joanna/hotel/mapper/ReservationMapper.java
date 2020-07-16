package com.joanna.hotel.mapper;

import com.joanna.hotel.dto.ReservationCreationDto;
import com.joanna.hotel.dto.ReservationDto;
import com.joanna.hotel.model.Reservation;
import com.joanna.hotel.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(target = "roomNumber", source = "room.roomNumber")
    ReservationDto reservationToReservationDto(Reservation reservation);

    @Mapping(target = "room", source = "room")
    @Mapping(target = "id", ignore = true)
    Reservation reservationCreationDtoAndRoomToReservation(ReservationCreationDto reservationCreationDto, Room room);

}
