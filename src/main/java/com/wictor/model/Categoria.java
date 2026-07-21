package com.wictor.model;

import com.wictor.enums.Role;
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

    @Column(name = "Catg_nome", nullable = false)
    private String nome;

    @Column(name = "Catg_sal", nullable = false)
    private BigDecimal salario;

    @Enumerated(EnumType.STRING)
    @Column(name = "Catg_role", nullable = false)
    private Role role;
}

