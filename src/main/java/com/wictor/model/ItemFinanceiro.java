package com.wictor.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Item_Financeiro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemFinanceiro {

    @EmbeddedId
    private ItemFinanceiroId id;

    @ManyToOne
    @MapsId("finId")
    @JoinColumn(name = "Fin_id", nullable = false)
    private Financeiro financeiro;

    @ManyToOne
    @MapsId("prodId")
    @JoinColumn(name = "Prod_id", nullable = false)
    private Produto produto;

    @Column(name = "ItemFin_qtd", nullable = false)
    private Integer quantidade;

    @Column(name = "ItemFin_val_unit", nullable = false)
    private BigDecimal valorUnitario;

    @Column(name = "ItemFin_desconto", nullable = false)
    private BigDecimal desconto;
}