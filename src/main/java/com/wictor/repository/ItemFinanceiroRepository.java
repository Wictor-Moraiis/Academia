package com.wictor.repository;

import com.wictor.model.ItemFinanceiro;
import com.wictor.model.ItemFinanceiroId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemFinanceiroRepository extends JpaRepository<ItemFinanceiro, ItemFinanceiroId> {

}
