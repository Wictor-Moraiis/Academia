package com.wictor.dto.venda;

import com.wictor.dto.itemfinanceiro.ItemFinanceiroDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Dados necessários para registrar uma venda")
public record VendaDto(

        @Schema(description = "Lista de itens da venda")
        @NotNull(message = "Itens da venda são obrigatórios")
        @Size(min = 1, message = "A venda deve ter pelo menos 1 item")
        List<ItemFinanceiroDto> itens
){}