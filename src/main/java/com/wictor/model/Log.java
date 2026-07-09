package com.wictor.model;

import com.wictor.enums.AcaoLog;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Log_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "Log_acao", nullable = false)
    private AcaoLog acao;

    @Column(name = "Log_entidade", nullable = false)
    private String entidade;

    @Column(name = "Log_entidade_id")
    private Integer entidadeId;

    @Column(name = "Log_descricao", nullable = false, length = 1000)
    private String descricao;

    @Column(name = "Log_sucesso", nullable = false)
    private Boolean sucesso;

    @Column(name = "Log_metodo", nullable = false, length = 10)
    private String metodo;

    @Column(name = "Log_url", nullable = false)
    private String url;

    @Column(name = "Log_ip", nullable = false)
    private String ip;

    @Column(name = "Log_data", nullable = false)
    private LocalDateTime data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id", nullable = false)
    private User usuario;
}