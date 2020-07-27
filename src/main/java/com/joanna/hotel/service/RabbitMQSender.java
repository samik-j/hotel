package com.joanna.hotel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joanna.hotel.event.RatingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQSender {

	private final RabbitTemplate rabbitTemplate;

	@Value("${hotel.rabbitmq.exchange}")
	private String exchange;

	@Autowired
	public RabbitMQSender(RabbitTemplate rabbitTemplate){
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendRoomEvent(String message) {
		log.info("Sending room event " + message);
		Message mes = MessageBuilder.withBody(message.getBytes()).build();
		rabbitTemplate.convertAndSend(exchange, "hotel.room-events.event", message);
	}

	public void sendRoomRatedEvent(RatingEvent ratingEvent) throws JsonProcessingException {
		log.info("Sending room rating event " + ratingEvent);
		ObjectMapper objectMapper = new ObjectMapper();
//		Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(ratingEvent)).setContentType(MediaType.APPLICATION_JSON_VALUE).build();
//		log.info("sending message");
		rabbitTemplate.convertAndSend(exchange, "hotel.room-events.rating", objectMapper.writeValueAsString(ratingEvent));
	}
}