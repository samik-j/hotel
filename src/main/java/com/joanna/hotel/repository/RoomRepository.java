package com.joanna.hotel.repository;

import com.joanna.hotel.model.Room;
import com.joanna.hotel.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByRoomType(RoomType roomType);

}
