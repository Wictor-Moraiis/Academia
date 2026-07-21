package com.wictor.integration.abacate.webhook;

import com.wictor.integration.abacate.dto.WebhookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class AbacateWebhookController {

    private final AbacateWebhookService abacateWebhookService;
    private final AbacateWebhookValidator validator;

    @PostMapping("/abacate")
    public ResponseEntity<Void> receber(@RequestParam String webhookSecret, @RequestBody WebhookRequest request) {

        if (!validator.validarSecret(webhookSecret)){return ResponseEntity.status(401).build();}

        abacateWebhookService.processar(request);

        return ResponseEntity.ok().build();
    }
}
