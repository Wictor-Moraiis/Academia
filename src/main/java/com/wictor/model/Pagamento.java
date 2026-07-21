package com.wictor.model;

import com.wictor.enums.FormaPagamento;
import com.wictor.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Pag_id")
    private Integer id;

    @Column(name = "Pag_abacate_bill_id", nullable = false)
    private String abacateBillId;

    @Column(name = "Pag_abacate_subscription_id")
    private String abacateSubscriptionId;

    @Column(name = "Pag_external_id", unique = true)
    private String externalId;

    @Column(name = "Pag_data", nullable = false)
    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    @Column(name = "Pag_forma")
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "Pag_status", nullable = false)
    private StatusPagamento status;

    @ManyToOne
    @JoinColumn(name = "Aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "Plano_id", nullable = false)
    private Plano plano;

    @OneToOne(mappedBy = "pagamento")
    private Financeiro financeiro;
}