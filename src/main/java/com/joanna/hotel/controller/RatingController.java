package com.joanna.hotel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joanna.hotel.dto.RatingDto;
import com.joanna.hotel.client.RatingClient;
import com.joanna.hotel.mapper.RatingMapper;
import com.joanna.hotel.service.RabbitMQSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingClient ratingClient;
    private final RabbitMQSender rabbitMQSender;

    @Value ("${test.property}")
    private String test;

    @Autowired
    public RatingController(RatingClient ratingClient, RabbitMQSender rabbitMQSender) {
        this.ratingClient = ratingClient;
        this.rabbitMQSender = rabbitMQSender;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RatingDto> getRatings(@RequestParam(required = false) Optional<Long> roomId) {
        log.info("test property " + test);
        if (roomId.isPresent()) {
            return ratingClient.getRatingsByRoomId(roomId.get());
        }
        return ratingClient.getAllRatings();
    }

    @PutMapping(value = "/rate")
    public void rateRoom(@RequestBody RatingDto ratingDto) throws JsonProcessingException {
        rabbitMQSender.sendRoomRatedEvent(RatingMapper.INSTANCE.ratingDtoToRatingEvent(ratingDto));
    }

}
