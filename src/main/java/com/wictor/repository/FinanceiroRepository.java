package com.wictor.repository;

import com.wictor.model.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceiroRepository extends JpaRepository<Financeiro, Integer> {
}
