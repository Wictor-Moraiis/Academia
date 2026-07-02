package com.wictor.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Prod_id")
    private Integer id;

    @Column(name = "Prod_nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "Prod_desc")
    private String desc;

    @Column(name = "Prod_preco", nullable = false)
    private BigDecimal preco;

    @Column(name = "Prod_qtd", nullable = false)
    private Integer qtd;

    @Column(name = "Prod_qtd_min", nullable = false)
    private Integer qtd_min;

    @Column(name = "Prod_foto")
    private String foto;

    @Column(name = "Prod_ativo", nullable = false)
    private boolean ativo;

}
