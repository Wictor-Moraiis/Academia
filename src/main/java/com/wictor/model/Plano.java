package com.wictor.model;

import com.wictor.enums.CicloPlano;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "Plano_ciclo", nullable = false)
    private CicloPlano ciclo;

    @Column(name = "Plano_abacate_id")
    private String abacateProductId;

    @Column(name = "Plano_recorrente", nullable = false)
    private boolean recorrente;

    @Column(name = "Plano_ativo", nullable = false)
    private boolean ativo;
}

