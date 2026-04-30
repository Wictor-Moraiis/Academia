package com.wictor.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Maquina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Maquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Maq_id")
    private Integer id;

    @Column(name = "Maq_nome", nullable = false)
    private String nome;

    @Column(name = "Maq_ativa", nullable = false)
    private boolean ativa;

}

