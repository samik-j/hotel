package com.joanna.hotel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    protected ClientCredentialsResourceDetails oAuthDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @LoadBalanced
    @Bean
    protected OAuth2RestTemplate restTemplate() {
        return new OAuth2RestTemplate(oAuthDetails());
    }
}
