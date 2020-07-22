package com.joanna.hotel.client;

import com.joanna.hotel.dto.RatingDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
public class RatingClient {

    private final RestTemplate restTemplate;

    @Value("${client.rating.base-url}")
    private String baseUrl;

    @Autowired
    public RatingClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RatingDto> getAllRatings() {
        String url = baseUrl + "/ratings";
        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RatingDto>>() {})
                           .getBody();
    }

    public List<RatingDto> getRatingsByRoomId(Long roomId) {
        String url = baseUrl + "/ratings/findByRoomId?roomId={roomId}";
        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RatingDto>>() {}, roomId)
                           .getBody();
    }

}
