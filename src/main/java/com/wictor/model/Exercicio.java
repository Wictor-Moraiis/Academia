package com.wictor.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Exercicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Exerc_id")
    private Integer id;

    @Column(name = "Exerc_nome", nullable = false)
    private String nome;

    @Column(name = "Exerc_obs")
    private String obs;

    @Column(name = "Exerc_foto")
    private String foto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Exerc_maq")
    private Maquina maquina;
}