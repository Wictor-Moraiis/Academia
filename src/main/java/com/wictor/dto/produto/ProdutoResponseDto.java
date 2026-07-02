package com.wictor.dto.produto;

import java.math.BigDecimal;

public record ProdutoResponseDto(

        Integer id,

        String nome,

        String desc,

        BigDecimal preco,

        Integer qtd,

        Integer qtd_min,

        String foto,

        boolean ativo

){}
