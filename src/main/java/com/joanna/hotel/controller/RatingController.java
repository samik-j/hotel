package com.joanna.hotel.controller;

import com.joanna.hotel.client.RatingClient;
import com.joanna.hotel.dto.RatingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingClient ratingClient;

    @Autowired
    public RatingController(RatingClient ratingClient) {
        this.ratingClient = ratingClient;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RatingDto> getAllRatings() {
        return ratingClient.getAllRatings();
    }

    @GetMapping(params = {"roomId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RatingDto> getRatingsByRoomId(@RequestParam Long roomId) {
        return ratingClient.getRatingsByRoomId(roomId);
    }

}
