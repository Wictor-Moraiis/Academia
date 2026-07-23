package com.wictor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wictor.enums.TipoContaBancaria;
import com.wictor.enums.TipoContrato;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "Funcionario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcionario {

    @Id
    @Column(name = "Func_id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "Func_id" , nullable = false)
    private User user;

    @Column(name = "Func_cref", unique = true)
    private String cref;

    @Enumerated(EnumType.STRING)
    @Column(name = "Func_tipo", nullable = false)
    private TipoContrato tipoContrato;

    @Column(name = "Func_turno_ini", nullable = false)
    private LocalTime turnoIni;

    @Column(name = "Func_turno_fim", nullable = false)
    private LocalTime turnoFim;

    @Column(name = "Func_banco", nullable = false)
    private String banco;

    @Column(name = "Func_agencia", nullable = false)
    private String agencia;

    @Column(name = "Func_conta", nullable = false)
    private String conta;

    @Enumerated(EnumType.STRING)
    @Column(name = "Func_tipo_conta", nullable = false)
    private TipoContaBancaria tipoConta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Catg_id", nullable = false)
    private Categoria categoria;
}