package com.wictor.controller;

import com.wictor.config.swagger.annotation.ApiErrorResponses;
import com.wictor.dto.financeiro.FinanceiroDto;
import com.wictor.dto.financeiro.FinanceiroResponseDto;
import com.wictor.dto.financeiro.FinanceiroUpdateDto;
import com.wictor.dto.venda.VendaDto;
import com.wictor.dto.venda.VendaResponseDto;
import com.wictor.security.CustomUserDetails;
import com.wictor.service.FinanceiroRelatorioService;
import com.wictor.service.FinanceiroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(
        name = "Financeiro",
        description = """
                Endpoints responsáveis pelo gerenciamento financeiro
                da academia.

                Permissões:
                • ADMIN: acesso completo.
                • GERENTE: gerenciamento financeiro e relatórios.
                • RECEPCIONISTA: cadastro, vendas e consultas.
                """
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/financeiros")
public class FinanceiroController {

    private final FinanceiroService financeiroService;
    private final FinanceiroRelatorioService financeiroRelatorioService;

    public FinanceiroController(FinanceiroService financeiroService,
                                FinanceiroRelatorioService financeiroRelatorioService) {
        this.financeiroService = financeiroService;
        this.financeiroRelatorioService = financeiroRelatorioService;
    }

    @Operation(
            summary = "Cadastrar movimentação financeira",
            description = """
                    Registra uma nova movimentação financeira manual.

                    Utilizado para lançamentos como:
                    • receitas extras;
                    • despesas;
                    • pagamentos;
                    • outros registros financeiros.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(
            responseCode = "201",
            description = "Movimentação cadastrada com sucesso.",
            content = @Content(schema = @Schema(implementation = FinanceiroResponseDto.class))
    )
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @PostMapping
    public ResponseEntity<FinanceiroResponseDto> cadastrar(@RequestBody @Valid FinanceiroDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.status(201).body(financeiroService.cadastrar(dto, user));
    }

    @Operation(
            summary = "Registrar venda",
            description = """
                    Realiza uma venda de produto.

                    O sistema valida o estoque,
                    registra a movimentação financeira
                    e atualiza a quantidade disponível.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "201", description = "Venda realizada com sucesso.",
            content = @Content(schema = @Schema(implementation = VendaResponseDto.class)))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @PostMapping("/venda")
    public ResponseEntity<VendaResponseDto> vender(@RequestBody @Valid VendaDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.status(201).body(financeiroService.vender(dto, user));
    }

    @Operation(
            summary = "Atualizar movimentação financeira",
            description = """
                    Atualiza os dados de uma movimentação financeira existente.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Movimentação atualizada com sucesso.",
            content = @Content(schema = @Schema(implementation = FinanceiroResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Movimentação financeira não encontrada.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @PatchMapping("/{financeiroId}")
    public ResponseEntity<FinanceiroResponseDto> atualizar(
            @Parameter(description = "Identificador único da movimentação.", example = "1")
            @PathVariable Integer financeiroId,
            @RequestBody @Valid FinanceiroUpdateDto dto) {

        return ResponseEntity.ok(financeiroService.atualizar(financeiroId, dto));
    }

    @Operation(
            summary = "Excluir movimentação financeira",
            description = """
                    Remove uma movimentação financeira.

                    Operação exclusiva para administradores.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "204", description = "Movimentação excluída com sucesso.")
    @ApiResponse(responseCode = "404", description = "Movimentação não encontrada.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{financeiroId}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador único da movimentação.", example = "1")
            @PathVariable Integer financeiroId) {

        financeiroService.deletar(financeiroId);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar movimentações financeiras",
            description = """
                    Retorna todas as movimentações financeiras permitidas
                    para o usuário autenticado.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.",
            content = @Content(mediaType = "application/json",array = @ArraySchema(
                    schema = @Schema(implementation = FinanceiroResponseDto.class))))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @GetMapping
    public ResponseEntity<List<FinanceiroResponseDto>> listar(
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(financeiroService.listar(user));
    }

    @Operation(
            summary = "Buscar movimentação por ID",
            description = """
                    Retorna uma movimentação financeira específica.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Movimentação encontrada.",
            content = @Content(schema = @Schema(implementation = FinanceiroResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Movimentação não encontrada.")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','RECEPCIONISTA')")
    @GetMapping("/{financeiroId}")
    public ResponseEntity<FinanceiroResponseDto> buscarPorId(
            @Parameter(description = "Identificador único da movimentação.", example = "1")
            @PathVariable Integer financeiroId,
            @AuthenticationPrincipal CustomUserDetails user) {

        return ResponseEntity.ok(financeiroService.buscarPorId(financeiroId, user));
    }

    @Operation(
            summary = "Gerar relatório financeiro",
            description = """
                    Gera um relatório financeiro em formato PDF
                    baseado no período informado.

                    O arquivo retornado contém os dados financeiros
                    entre a data inicial e final.
                    """
    )
    @ApiErrorResponses
    @ApiResponse(responseCode = "200", description = "Relatório PDF gerado com sucesso.",
            content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE))
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @GetMapping("/relatorio")
    public ResponseEntity<byte[]> gerarRelatorio(
            @Parameter(description = "Data inicial do relatório.", example = "2026-01-01")
            @RequestParam LocalDate inicio,
            @Parameter(description = "Data final do relatório.", example = "2026-12-31")
            @RequestParam LocalDate fim) {

        byte[] pdf = financeiroRelatorioService.gerarRelatorio(inicio, fim);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=relatorio-financeiro.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
