package com.joanna.hotel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joanna.hotel.event.RatingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${hotel.rabbitmq.exchange}")
    private String exchange;

    @Autowired
    public RabbitMQSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendRoomEvent(String message) {
        log.info("Sending room event " + message);
        rabbitTemplate.convertAndSend(exchange, "hotel.room-events.event", message);
    }

    public void sendRoomRatedEvent(RatingEvent ratingEvent) throws JsonProcessingException {
        log.info("Sending room rating event " + ratingEvent);
        rabbitTemplate.convertAndSend(exchange, "hotel.room-events.rating", objectMapper.writeValueAsString(ratingEvent));
    }
}