package com.joanna.hotel.controller;

import com.joanna.hotel.dto.RatingDto;
import com.joanna.hotel.client.RatingClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingClient ratingClient;

    @Value ("${test.property}")
    private String test;

    @Autowired
    public RatingController(RatingClient ratingClient) {
        this.ratingClient = ratingClient;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RatingDto> getRatings(@RequestParam(required = false) Optional<Long> roomId) {
        log.info("test property " + test);
        if (roomId.isPresent()) {
            return ratingClient.getRatingsByRoomId(roomId.get());
        }
        return ratingClient.getAllRatings();
    }

}
