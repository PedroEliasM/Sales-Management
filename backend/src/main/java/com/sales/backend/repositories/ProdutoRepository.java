package com.sales.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sales.backend.models.Produto;
import com.sales.backend.models.Usuario;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    List<Produto> findByFuncionarioCadastro(Usuario funcionario);
}