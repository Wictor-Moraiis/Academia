package com.wictor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Aluno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aluno {

    @Id
    @Column(name = "Aluno_id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "Aluno_id"  , nullable = false)
    private User user;

    @Column(name = "Aluno_saude")
    private String saude;

    @Column(name = "Aluno_obs")
    private String obs;

    @Column(name = "Aluno_altura", nullable = false)
    private BigDecimal altura;

    @Column(name = "Aluno_peso", nullable = false)
    private BigDecimal peso;

    @Column(name = "Aluno_obj", nullable = false)
    private String objetivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Aluno_plano", nullable = false)
    private Plano plano;

    @Column(name = "Aluno_vencimento", nullable = false)
    private LocalDate vencimento;

    @Column(name = "Aluno_plano_vencido", nullable = false)
    private boolean vencido;

}