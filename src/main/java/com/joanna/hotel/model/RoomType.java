package com.joanna.hotel.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum RoomType {
    BASIC(4),
    SUITE(6),
    PENTHOUSE(8);

    public static final List<RoomType> sortedByCapacity = new ArrayList<>();
    public static final Comparator<RoomType> ROOM_TYPE_COMPARATOR = (type1, type2) -> {
        if (type1.capacity > type2.capacity) {
            return 1;
        } else if (type1.capacity.equals(type2.capacity)) {
            return 0;
        }
        return -1;
    };

    static {
        sortedByCapacity.addAll(Arrays.stream(RoomType.values()).sorted(ROOM_TYPE_COMPARATOR).collect(Collectors.toList()));
    }

    public final Integer capacity;

    RoomType(Integer capacity) {
        this.capacity = capacity;
    }
}
