package com.sales.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sales.backend.models.Estoque;
import com.sales.backend.models.Produto;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
    Optional<Estoque> findByProduto(Produto produto);
    Optional<Estoque> findByProdutoId(Integer produtoId);
}