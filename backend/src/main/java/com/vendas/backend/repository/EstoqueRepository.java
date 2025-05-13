package com.vendas.backend.repository;

import com.vendas.backend.model.Estoque;
import com.vendas.backend.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
    Optional<Estoque> findByProduto(Produto produto);
    Optional<Estoque> findByProdutoId(Integer produtoId);
}