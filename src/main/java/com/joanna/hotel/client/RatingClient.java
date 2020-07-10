package com.joanna.hotel.client;

import com.joanna.hotel.dto.RatingDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class RatingClient {

    public List<RatingDto> getAllRatings() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/ratings";
        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RatingDto>>(){})
                           .getBody();
    }

    public List<RatingDto> getRatingsByRoomId(Long roomId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/ratings?roomId={roomId}";
        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RatingDto>>(){}, roomId)
                           .getBody();
    }
}
