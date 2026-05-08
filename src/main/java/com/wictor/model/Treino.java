package com.wictor.model;

import com.wictor.enums.ObjetivoTreino;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Treino")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Treino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Treino_id")
    private Integer id;

    @Column(name = "Treino_nome", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "Treino_obj", nullable = false)
    private ObjetivoTreino ObjTreino;

    @Column(name = "Treino_inicio", nullable = false)
    private LocalDate inicio;

    @Column(name = "Treino_fim", nullable = false)
    private LocalDate fim;

    @Column(name = "Treino_criado", nullable = false)
    private LocalDate criado;

    @Column(name = "Treino_modificado", nullable = false)
    private LocalDate modificado;

    @Column(name = "Treino_obs")
    private String obs;

    @Column(name = "Treino_ativo", nullable = false)
    private boolean ativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Aluno_id")
    private Aluno aluno;
}