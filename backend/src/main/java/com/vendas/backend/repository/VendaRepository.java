package com.vendas.backend.repository;

import com.vendas.backend.model.Usuario;
import com.vendas.backend.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Integer> {
    List<Venda> findByCliente(Usuario cliente);
}