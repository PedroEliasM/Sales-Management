package com.vendas.backend.service;

import com.vendas.backend.dto.ProdutoDTO;
import com.vendas.backend.model.Estoque;
import com.vendas.backend.model.Produto;
import com.vendas.backend.model.Usuario;
import com.vendas.backend.repository.EstoqueRepository;
import com.vendas.backend.repository.ProdutoRepository;
import com.vendas.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EstoqueRepository estoqueRepository;

    public List<ProdutoDTO> listarTodos() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProdutoDTO buscarPorId(Integer id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        return produtoOptional.map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public ProdutoDTO salvar(ProdutoDTO produtoDTO) {
        Optional<Usuario> funcionarioOptional = usuarioRepository.findById(produtoDTO.getIdFuncionario());
        
        if (funcionarioOptional.isPresent()) {
            Produto produto = convertToEntity(produtoDTO);
            produto.setFuncionarioCadastro(funcionarioOptional.get());
            
            // Salva o produto
            produto = produtoRepository.save(produto);
            
            // Cria entrada no estoque
            Estoque estoque = new Estoque();
            estoque.setProduto(produto);
            estoque.setQuantidade(produtoDTO.getQuantidade() != null ? produtoDTO.getQuantidade() : 0);
            estoqueRepository.save(estoque);
            
            return convertToDTO(produto);
        }
        
        return null;
    }

    @Transactional
    public ProdutoDTO atualizar(Integer id, ProdutoDTO produtoDTO) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        
        if (produtoOptional.isPresent()) {
            Produto produto = produtoOptional.get();
            
            produto.setNome(produtoDTO.getNome());
            produto.setValor(produtoDTO.getValor());
            produto.setDescricao(produtoDTO.getDescricao());
            
            // Atualiza o produto
            produto = produtoRepository.save(produto);
            
            // Atualiza estoque se quantidade fornecida
            if (produtoDTO.getQuantidade() != null) {
                Optional<Estoque> estoqueOptional = estoqueRepository.findByProduto(produto);
                if (estoqueOptional.isPresent()) {
                    Estoque estoque = estoqueOptional.get();
                    estoque.setQuantidade(produtoDTO.getQuantidade());
                    estoqueRepository.save(estoque);
                }
            }
            
            return convertToDTO(produto);
        }
        
        return null;
    }

    @Transactional
    public boolean excluir(Integer id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);
        
        if (produtoOptional.isPresent()) {
            // Primeiro remove do estoque
            Optional<Estoque> estoqueOptional = estoqueRepository.findByProduto(produtoOptional.get());
            estoqueOptional.ifPresent(estoque -> estoqueRepository.delete(estoque));
            
            // Depois remove o produto
            produtoRepository.deleteById(id);
            return true;
        }
        
        return false;
    }

    public List<ProdutoDTO> listarPorFuncionario(Integer idFuncionario) {
        Optional<Usuario> funcionarioOptional = usuarioRepository.findById(idFuncionario);
        
        if (funcionarioOptional.isPresent()) {
            List<Produto> produtos = produtoRepository.findByFuncionarioCadastro(funcionarioOptional.get());
            return produtos.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        
        return List.of();
    }

    private ProdutoDTO convertToDTO(Produto produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setValor(produto.getValor());
        dto.setDescricao(produto.getDescricao());
        dto.setIdFuncionario(produto.getFuncionarioCadastro().getId());
        
        // Busca quantidade no estoque
        Optional<Estoque> estoqueOptional = estoqueRepository.findByProduto(produto);
        estoqueOptional.ifPresent(estoque -> dto.setQuantidade(estoque.getQuantidade()));
        
        return dto;
    }

    private Produto convertToEntity(ProdutoDTO dto) {
        Produto produto = new Produto();
        produto.setId(dto.getId());
        produto.setNome(dto.getNome());
        produto.setValor(dto.getValor());
        produto.setDescricao(dto.getDescricao());
        return produto;
    }
}