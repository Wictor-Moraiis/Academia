package com.wictor.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Catg_id")
    private Integer id;

    @Column(name = "Catg_sal", nullable = false)
    private BigDecimal salario;

    @Column(name = "Catg_nome", nullable = false)
    private String nome;
}

