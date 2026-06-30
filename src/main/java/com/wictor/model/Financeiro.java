package com.wictor.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Financeiro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Financeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Fin_id")
    private Integer id;

    @Column(name = "Fin_nome", nullable = false)
    private String nome;

    @Column(name = "Fin_tipo", nullable = false)
    private String tipo;

    @Column(name = "Fin_data", nullable = false)
    private LocalDate data;

    @Column(name = "Fin_val", nullable = false)
    private BigDecimal valor;
}
