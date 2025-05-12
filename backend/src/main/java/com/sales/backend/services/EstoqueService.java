package com.sales.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sales.backend.dto.EstoqueDTO;
import com.sales.backend.exception.ResourceNotFoundException;
import com.sales.backend.models.Estoque;
import com.sales.backend.models.Produto;
import com.sales.backend.repositories.EstoqueRepository;
import com.sales.backend.repositories.ProdutoRepository;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional(readOnly = true)
    public List<Estoque> listarTodos() {
        return estoqueRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Estoque buscarPorId(Integer id) {
        return estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado com ID: " + id));
    }
    
    @Transactional(readOnly = true)
    public Estoque buscarPorProdutoId(Integer produtoId) {
        return estoqueRepository.findByProdutoId(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado para o produto com ID: " + produtoId));
    }

    @Transactional
    public Estoque salvar(EstoqueDTO estoqueDTO) {
        Produto produto = produtoRepository.findById(estoqueDTO.getIdProduto())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        
        // Verificar se já existe estoque para este produto
        estoqueRepository.findByProduto(produto).ifPresent(e -> {
            throw new IllegalArgumentException("Já existe estoque para este produto");
        });
        
        Estoque estoque = new Estoque();
        estoque.setProduto(produto);
        estoque.setQuantidade(estoqueDTO.getQuantidade());
        estoque.setDataCadastro(LocalDateTime.now());
        estoque.setDataAtualizacao(LocalDateTime.now());
        
        return estoqueRepository.save(estoque);
    }

    @Transactional
    public Estoque atualizar(Integer id, EstoqueDTO estoqueDTO) {
        Estoque estoque = buscarPorId(id);
        
        estoque.setQuantidade(estoqueDTO.getQuantidade());
        estoque.setDataAtualizacao(LocalDateTime.now());
        
        return estoqueRepository.save(estoque);
    }
    
    @Transactional
    public Estoque atualizarQuantidade(Integer produtoId, Integer quantidade) {
        Estoque estoque = buscarPorProdutoId(produtoId);
        
        estoque.setQuantidade(quantidade);
        estoque.setDataAtualizacao(LocalDateTime.now());
        
        return estoqueRepository.save(estoque);
    }
    
    @Transactional
    public Estoque reduzirEstoque(Integer produtoId, Integer quantidade) {
        Estoque estoque = buscarPorProdutoId(produtoId);
        
        if (estoque.getQuantidade() < quantidade) {
            throw new IllegalArgumentException("Quantidade insuficiente em estoque");
        }
        
        estoque.setQuantidade(estoque.getQuantidade() - quantidade);
        estoque.setDataAtualizacao(LocalDateTime.now());
        
        return estoqueRepository.save(estoque);
    }

    @Transactional
    public void excluir(Integer id) {
        Estoque estoque = buscarPorId(id);
        estoqueRepository.delete(estoque);
    }
    
    public List<EstoqueDTO> converterParaDTO(List<Estoque> estoques) {
            return estoques.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }

    public EstoqueDTO converterParaDTO(Estoque estoque) {
        EstoqueDTO dto = new EstoqueDTO();
        dto.setId(estoque.getId());
        dto.setIdProduto(estoque.getProduto().getId());
        dto.setQuantidade(estoque.getQuantidade());
        return dto;
    }
}