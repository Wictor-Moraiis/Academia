package com.wictor.integration.abacate;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "abacate.api")
public record AbacateProperties(

        String url,
        String key
){}
