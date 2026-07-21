package com.wictor.dto.venda;

import com.wictor.dto.itemfinanceiro.ItemFinanceiroDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record VendaDto(

        @NotNull(message = "Itens da venda são obrigatórios")
        @Size(min = 1, message = "A venda deve ter pelo menos 1 item")
        List<ItemFinanceiroDto> itens
){}
