package com.wictor.integration.abacate.webhook;

import com.wictor.config.swagger.annotation.ApiErrorResponses;
import com.wictor.integration.abacate.dto.WebhookRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Webhooks",
        description = """
                Endpoints utilizados para integração
                com serviços externos de pagamento.

                Responsável por receber eventos enviados
                pelo AbacatePay e atualizar os pagamentos.
                """
)
@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class AbacateWebhookController {

    private final AbacateWebhookService abacateWebhookService;
    private final AbacateWebhookValidator validator;

    @Operation(
            summary = "Receber webhook do AbacatePay",
            description = """
                    Recebe notificações de eventos de pagamento
                    enviadas pelo AbacatePay.

                    A validação da requisição é realizada através
                    do webhookSecret fornecido pelo provedor.

                    Eventos processados:
                    • checkout.completed
                    • subscription.payment_succeeded
                    • subscription.completed
                    • subscription.cancelled
                    • subscription.plan_changed
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Webhook processado com sucesso.")
    @ApiResponse(responseCode = "401", description = "Webhook secret inválido.")
    @ApiResponse(responseCode = "400", description = "Payload do webhook inválido.", content = @Content)
    @PostMapping("/abacate")
    public ResponseEntity<Void> receber(

            @Parameter(description = "Secret utilizado para validar a origem do webhook.",
                    example = "whsec_xxxxxxxxx")
            @RequestParam String webhookSecret,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados enviados pelo AbacatePay contendo informações do evento.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = WebhookRequest.class))
            )
            @RequestBody WebhookRequest request) {

        if (!validator.validarSecret(webhookSecret)) {

            return ResponseEntity.status(401).build();
        }

        abacateWebhookService.processar(request);
        return ResponseEntity.ok().build();
    }
}