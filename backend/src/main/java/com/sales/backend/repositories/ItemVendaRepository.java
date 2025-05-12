package com.sales.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sales.backend.models.ItemVenda;
import com.sales.backend.models.Venda;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Integer> {
    List<ItemVenda> findByVenda(Venda venda);
    List<ItemVenda> findByVendaId(Integer vendaId);
}