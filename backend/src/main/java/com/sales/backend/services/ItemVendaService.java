package com.sales.backend.services;

import com.sales.backend.exception.ResourceNotFoundException;
import com.sales.backend.models.ItemVenda;
import com.sales.backend.repositories.ItemVendaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemVendaService {

    @Autowired
    private ItemVendaRepository itemVendaRepository;

    @Transactional(readOnly = true)
    public List<ItemVenda> listarTodos() {
        return itemVendaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ItemVenda buscarPorId(Integer id) {
        return itemVendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item da venda não encontrado com ID: " + id));
    }

    // Outras operações específicas para ItemVenda, se necessário
}