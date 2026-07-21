package com.wictor.config;

import com.wictor.integration.abacate.AbacateProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class AbacateConfig {

    @Bean
    public RestClient abacateRestClient(AbacateProperties properties) {

        return RestClient.builder()
                .baseUrl(properties.url())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.key())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}