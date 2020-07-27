package com.joanna.hotel.mapper;

import com.joanna.hotel.dto.RatingDto;
import com.joanna.hotel.dto.ReservationCreationDto;
import com.joanna.hotel.dto.ReservationDto;
import com.joanna.hotel.event.RatingEvent;
import com.joanna.hotel.model.Reservation;
import com.joanna.hotel.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RatingMapper {

    RatingMapper INSTANCE = Mappers.getMapper(RatingMapper.class);

    RatingEvent ratingDtoToRatingEvent(RatingDto ratingDto);

}
