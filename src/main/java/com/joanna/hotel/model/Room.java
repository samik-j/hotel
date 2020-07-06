package com.joanna.hotel.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
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
    private List<Reservation> reservations = new ArrayList<>();

    public Room(RoomType roomType, Integer roomNumber) {
        this.roomType = roomType;
        this.roomNumber = roomNumber;
    }
}
