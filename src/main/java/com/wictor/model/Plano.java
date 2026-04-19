package com.wictor.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Plano")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Plano_id")
    private Integer id;

    @Column(name = "Plano_nome", nullable = false)
    private String nome;

    @Column(name = "Plano_valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "Plano_validade", nullable = false)
    private Integer validade;
}

