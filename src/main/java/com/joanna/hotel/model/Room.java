package com.joanna.hotel.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Column(nullable = false)
    private Integer roomNumber;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    private List<Reservation> reservations;

    public Room(RoomType roomType, Integer roomNumber, List<Reservation> reservations) {
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this. reservations = reservations;
    }
}
