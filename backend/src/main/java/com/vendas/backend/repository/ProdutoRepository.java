package com.vendas.backend.repository;

import com.vendas.backend.model.Produto;
import com.vendas.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    List<Produto> findByFuncionarioCadastro(Usuario funcionario);
}