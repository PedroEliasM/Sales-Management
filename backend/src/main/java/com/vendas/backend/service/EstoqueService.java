package com.vendas.backend.service;

import com.vendas.backend.model.Estoque;
import com.vendas.backend.model.Produto;
import com.vendas.backend.repository.EstoqueRepository;
import com.vendas.backend.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Estoque> listarTodos() {
        return estoqueRepository.findAll();
    }

    public Estoque buscarPorProdutoId(Integer produtoId) {
        return estoqueRepository.findByProdutoId(produtoId).orElse(null);
    }

    @Transactional
    public boolean atualizarQuantidade(Integer produtoId, Integer quantidade) {
        Optional<Produto> produtoOptional = produtoRepository.findById(produtoId);
        
        if (produtoOptional.isPresent()) {
            Optional<Estoque> estoqueOptional = estoqueRepository.findByProduto(produtoOptional.get());
            
            if (estoqueOptional.isPresent()) {
                Estoque estoque = estoqueOptional.get();
                estoque.setQuantidade(quantidade);
                estoqueRepository.save(estoque);
                return true;
            } else {
                // Cria um novo estoque se não existir
                Estoque estoque = new Estoque();
                estoque.setProduto(produtoOptional.get());
                estoque.setQuantidade(quantidade);
                estoqueRepository.save(estoque);
                return true;
            }
        }
        
        return false;
    }

    @Transactional
    public boolean reduzirEstoque(Integer produtoId, Integer quantidade) {
        Optional<Estoque> estoqueOptional = estoqueRepository.findByProdutoId(produtoId);
        
        if (estoqueOptional.isPresent()) {
            Estoque estoque = estoqueOptional.get();
            
            // Verifica se há estoque suficiente
            if (estoque.getQuantidade() >= quantidade) {
                estoque.setQuantidade(estoque.getQuantidade() - quantidade);
                estoqueRepository.save(estoque);
                return true;
            }
        }
        
        return false;
    }
}