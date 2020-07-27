package com.joanna.hotel.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${hotel.rabbitmq.queue.room-events}")
    String roomEventsQueue;

    @Value("${hotel.rabbitmq.queue.room-rating}")
    String roomRatingQueue;

    @Value("${hotel.rabbitmq.exchange}")
    String exchange;

    @Value("${hotel.rabbitmq.queue.room-events.routing-key}")
    private String roomEventsRoutingKey;

    @Value("${hotel.rabbitmq.queue.room-rating.routing-key}")
    private String roomRatingRoutingKey;

    @Bean
    Queue roomEventsQueue() {
        return new Queue(roomEventsQueue, false);
    }

    @Bean
    Queue roomRatingQueue() {
        return new Queue(roomRatingQueue, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Binding binding1() {
        return BindingBuilder.bind(roomEventsQueue()).to(exchange()).with(roomEventsRoutingKey);
    }

    @Bean
    Binding binding2() {
        return BindingBuilder.bind(roomRatingQueue()).to(exchange()).with(roomRatingRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
