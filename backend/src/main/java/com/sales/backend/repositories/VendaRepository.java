package com.sales.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sales.backend.models.Usuario;
import com.sales.backend.models.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Integer> {
    List<Venda> findByCliente(Usuario cliente);
    List<Venda> findByClienteId(Integer clienteId);
}