package com.wictor.integration.abacate.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AbacateWebhookValidator {

    private final String secret;

    public AbacateWebhookValidator(@Value("${abacate.webhook.secret}") String secret) {this.secret = secret;}

    public boolean validarSecret(String recebido) {return secret.equals(recebido);}
}