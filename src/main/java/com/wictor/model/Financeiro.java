package com.wictor.model;

import com.wictor.enums.OrigemFinanceiro;
import com.wictor.enums.TipoFinanceiro;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Financeiro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Financeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Fin_id")
    private Integer id;

    @Column(name = "Fin_nome", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "Fin_tipo", nullable = false)
    private TipoFinanceiro tipo;

    @Column(name = "Fin_data", nullable = false)
    private LocalDate data;

    @Column(name = "Fin_val", nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "Fin_origem", nullable = false)
    private OrigemFinanceiro origem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Func_id")
    private Funcionario funcionario;

    @OneToOne
    @JoinColumn(name = "Pag_id")
    private Pagamento pagamento;

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL)
    private List<ItemFinanceiro> itens;
}
