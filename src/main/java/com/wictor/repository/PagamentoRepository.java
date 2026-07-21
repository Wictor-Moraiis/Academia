package com.wictor.repository;

import com.wictor.enums.StatusPagamento;
import com.wictor.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    List<Pagamento> findByAlunoIdOrderByDataDesc(Integer alunoId);
    Optional<Pagamento> findByExternalId(String externalId);
    Optional<Pagamento> findByAbacateBillId(String id);
    Optional<Pagamento> findFirstByAbacateSubscriptionIdOrderByDataDesc(String id);
    Optional<Pagamento> findFirstByAlunoIdAndStatusOrderByDataDesc(Integer alunoId, StatusPagamento status);
    Optional<Pagamento> findFirstByAlunoIdAndAbacateSubscriptionIdIsNotNullOrderByDataDesc(Integer alunoId);
    Optional<Pagamento> findFirstByAbacateSubscriptionIdAndStatusOrderByDataDesc(String subscriptionId, StatusPagamento status);
}