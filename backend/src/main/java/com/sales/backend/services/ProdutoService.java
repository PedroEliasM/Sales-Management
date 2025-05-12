package com.sales.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sales.backend.dto.ProdutoDTO;
import com.sales.backend.exception.ResourceNotFoundException;
import com.sales.backend.models.Estoque;
import com.sales.backend.models.Produto;
import com.sales.backend.models.Usuario;
import com.sales.backend.repositories.EstoqueRepository;
import com.sales.backend.repositories.ProdutoRepository;
import com.sales.backend.repositories.UsuarioRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EstoqueRepository estoqueRepository;

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
    }

    @Transactional
    public Produto salvar(ProdutoDTO produtoDTO) {
        Usuario funcionario = usuarioRepository.findById(produtoDTO.getIdFuncionarioCadastro())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
        
        Produto produto = new Produto();
        produto.setNome(produtoDTO.getNome());
        produto.setValor(produtoDTO.getValor());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setFuncionarioCadastro(funcionario);
        produto.setDataCadastro(LocalDateTime.now());
        
        produto = produtoRepository.save(produto);
        
        // Criar estoque para o produto se a quantidade for informada
        if (produtoDTO.getQuantidadeEstoque() != null) {
            Estoque estoque = new Estoque();
            estoque.setProduto(produto);
            estoque.setQuantidade(produtoDTO.getQuantidadeEstoque());
            estoque.setDataCadastro(LocalDateTime.now());
            estoque.setDataAtualizacao(LocalDateTime.now());
            estoqueRepository.save(estoque);
        }
        
        return produto;
    }

    @Transactional
    public Produto atualizar(Integer id, ProdutoDTO produtoDTO) {
        Produto produto = buscarPorId(id);

        // Atualizar os campos do produto
        produto.setNome(produtoDTO.getNome());
        produto.setValor(produtoDTO.getValor());
        produto.setDescricao(produtoDTO.getDescricao());
        
        // Salvar o produto atualizado
        Produto produtoAtualizado = produtoRepository.save(produto);
        
        // Atualizar estoque se necessário
        if (produtoDTO.getQuantidadeEstoque() != null) {
            estoqueRepository.findByProduto(produtoAtualizado).ifPresentOrElse(
                estoque -> {
                    estoque.setQuantidade(produtoDTO.getQuantidadeEstoque());
                    estoque.setDataAtualizacao(LocalDateTime.now());
                    estoqueRepository.save(estoque);
                },
                () -> {
                    Estoque novoEstoque = new Estoque();
                    novoEstoque.setProduto(produtoAtualizado);
                    novoEstoque.setQuantidade(produtoDTO.getQuantidadeEstoque());
                    novoEstoque.setDataCadastro(LocalDateTime.now());
                    novoEstoque.setDataAtualizacao(LocalDateTime.now());
                    estoqueRepository.save(novoEstoque);
                }
            );
        }
        
        return produtoAtualizado;
    }

    @Transactional
    public void excluir(Integer id) {
        Produto produto = buscarPorId(id);
        
        // Remover do estoque primeiro
        estoqueRepository.findByProduto(produto).ifPresent(estoqueRepository::delete);
        
        produtoRepository.delete(produto);
    }
    
    public List<ProdutoDTO> converterParaDTO(List<Produto> produtos) {
        return produtos.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }
    
    public ProdutoDTO converterParaDTO(Produto produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setValor(produto.getValor());
        dto.setDescricao(produto.getDescricao());
        dto.setIdFuncionarioCadastro(produto.getFuncionarioCadastro().getId());
        
        // Buscar quantidade no estoque
        estoqueRepository.findByProduto(produto).ifPresent(
            estoque -> dto.setQuantidadeEstoque(estoque.getQuantidade())
        );
        
        return dto;
    }
}