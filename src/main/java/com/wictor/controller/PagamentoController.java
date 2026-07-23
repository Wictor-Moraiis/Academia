package com.wictor.controller;

import com.wictor.config.swagger.annotation.ApiErrorResponses;
import com.wictor.dto.pagamento.AlterarPlanoResponseDto;
import com.wictor.dto.pagamento.CheckoutPagamentoResponseDto;
import com.wictor.dto.pagamento.PagamentoPresencialDto;
import com.wictor.dto.pagamento.PagamentoResponseDto;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Pagamentos",
        description = """
                Endpoints responsáveis pelo gerenciamento de pagamentos da academia.

                Permissões:
                • ADMIN: gerenciamento completo.
                • GERENTE: gerenciamento financeiro.
                • RECEPCIONISTA: pagamentos presenciais e consultas.
                • ALUNO: pagamentos próprios, alteração e cancelamento de assinatura.
                """
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @Operation(
            summary = "Registrar pagamento presencial",
            description = """
                    Registra manualmente um pagamento realizado presencialmente
                    na academia.

                    Utilizado para evitar cobrança de taxas do gateway de pagamento.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "201", description = "Pagamento registrado com sucesso.",
            content = @Content(schema = @Schema(implementation = PagamentoResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @PostMapping("/presencial")
    public ResponseEntity<PagamentoResponseDto> pagarPresencial(
            @RequestBody @Valid PagamentoPresencialDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.status(201).body(pagamentoService.pagamentoPresencial(dto, user));
    }

    @Operation(
            summary = "Gerar checkout de pagamento",
            description = """
                    Cria um checkout de pagamento para contratação de um plano.

                    O pagamento pode ser realizado através do gateway integrado.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "201", description = "Checkout criado com sucesso.",
            content = @Content(schema = @Schema(implementation = CheckoutPagamentoResponseDto.class)))
    @PreAuthorize("hasRole('ALUNO')")
    @PostMapping("/checkout/{planoId}")
    public ResponseEntity<CheckoutPagamentoResponseDto> checkout(
            @Parameter(description = "Identificador do plano a ser contratado.", example = "1")
            @PathVariable Integer planoId,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.status(201).body(pagamentoService.checkout(planoId, user));
    }

    @Operation(
            summary = "Cancelar assinatura",
            description = """
                    Solicita o cancelamento da assinatura ativa do aluno.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Assinatura cancelada com sucesso.")
    @PreAuthorize("hasRole('ALUNO')")
    @PostMapping("/assinatura/cancelar")
    public ResponseEntity<Void> cancelarAssinatura(@AuthenticationPrincipal CustomUserDetails user) {

        pagamentoService.cancelarAssinatura(user);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Alterar plano da assinatura",
            description = """
                    Realiza a troca do plano atual do aluno.

                    Disponível apenas para assinaturas recorrentes.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Plano alterado com sucesso.",
            content = @Content(schema = @Schema(implementation = AlterarPlanoResponseDto.class)))
    @PreAuthorize("hasRole('ALUNO')")
    @PostMapping("/assinatura/alterar/{planoId}")
    public ResponseEntity<AlterarPlanoResponseDto> alterarPlano(
            @Parameter(description = "Identificador do plano a ser contratado.", example = "1")
            @PathVariable Integer planoId,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(pagamentoService.alterarPlano(planoId, user));
    }

    @Operation(
            summary = "Listar pagamentos",
            description = """
                    Retorna os pagamentos vinculados ao usuário autenticado.

                    Administradores, gerentes e recepcionistas possuem acesso
                    ao gerenciamento financeiro.

                    Alunos visualizam apenas seus próprios pagamentos.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Pagamentos retornados com sucesso.",
            content = @Content(mediaType = "application/json",array = @ArraySchema(
                    schema = @Schema(implementation = PagamentoResponseDto.class))))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','ALUNO')")
    @GetMapping
    public ResponseEntity<List<PagamentoResponseDto>> listar(
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(pagamentoService.listar(user));
    }

    @Operation(
            summary = "Buscar pagamento por ID",
            description = """
                    Retorna os detalhes de um pagamento específico.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Pagamento encontrado.",
            content = @Content(schema = @Schema(implementation = PagamentoResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA','ALUNO')")
    @GetMapping("/{pagamentoId}")
    public ResponseEntity<PagamentoResponseDto> buscarPorId(
            @PathVariable Integer pagamentoId,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(pagamentoService.buscarPorId(pagamentoId, user));
    }
}