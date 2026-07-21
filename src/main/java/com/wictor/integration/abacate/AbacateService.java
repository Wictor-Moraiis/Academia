package com.wictor.integration.abacate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class AbacateService {

    private final RestClient abacateRestClient;
}
