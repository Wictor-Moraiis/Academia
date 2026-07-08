package com.wictor.repository;

import com.wictor.model.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FinanceiroRepository extends JpaRepository<Financeiro, Integer> {

    List<Financeiro> findByDataBetweenOrderByDataAsc(LocalDate inicio, LocalDate fim);
}
