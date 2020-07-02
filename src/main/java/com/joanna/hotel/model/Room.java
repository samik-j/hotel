package com.joanna.hotel.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Data
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

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;
}
