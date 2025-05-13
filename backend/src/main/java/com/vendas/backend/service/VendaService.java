package com.vendas.backend.service;

import com.vendas.backend.dto.VendaDTO;
import com.vendas.backend.model.*;
import com.vendas.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;
    
    @Autowired
    private ItemVendaRepository itemVendaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private EstoqueService estoqueService;

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    public Venda buscarPorId(Integer id) {
        return vendaRepository.findById(id).orElse(null);
    }

    public List<Venda> listarPorCliente(Integer idCliente) {
        Optional<Usuario> clienteOptional = usuarioRepository.findById(idCliente);
        return clienteOptional.map(vendaRepository::findByCliente).orElse(List.of());
    }

    @Transactional
    public Venda realizarVenda(VendaDTO vendaDTO) {
        Optional<Usuario> clienteOptional = usuarioRepository.findById(vendaDTO.getIdCliente());
        
        if (clienteOptional.isPresent() && vendaDTO.getItens() != null && !vendaDTO.getItens().isEmpty()) {
            Usuario cliente = clienteOptional.get();
            
            // Cria nova venda
            Venda venda = new Venda();
            venda.setCliente(cliente);
            venda.setValorTotal(BigDecimal.ZERO);
            
            // Salva venda para obter ID
            venda = vendaRepository.save(venda);
            
            List<ItemVenda> itensVenda = new ArrayList<>();
            BigDecimal valorTotal = BigDecimal.ZERO;
            
            // Processa cada item
            for (VendaDTO.ItemVendaDTO itemDTO : vendaDTO.getItens()) {
                Optional<Produto> produtoOptional = produtoRepository.findById(itemDTO.getIdProduto());
                
                if (produtoOptional.isPresent() && itemDTO.getQuantidade() > 0) {
                    Produto produto = produtoOptional.get();
                    
                    // Verifica e reduz estoque
                    if (estoqueService.reduzirEstoque(produto.getId(), itemDTO.getQuantidade())) {
                        ItemVenda itemVenda = new ItemVenda();
                        itemVenda.setVenda(venda);
                        itemVenda.setProduto(produto);
                        itemVenda.setQuantidadeVendida(itemDTO.getQuantidade());
                        
                        // Usa o valor informado na venda ou o valor do produto
                        BigDecimal valorUnitario = itemDTO.getValorUnitario() != null 
                                ? itemDTO.getValorUnitario() 
                                : produto.getValor();
                        
                        itemVenda.setValorUnitarioNaVenda(valorUnitario);
                        
                        // Calcula valor total do item
                        BigDecimal valorTotalItem = valorUnitario.multiply(
                                BigDecimal.valueOf(itemDTO.getQuantidade()));
                        
                        itemVenda.setValorTotalItem(valorTotalItem);
                        itemVenda = itemVendaRepository.save(itemVenda);
                        
                        itensVenda.add(itemVenda);
                        valorTotal = valorTotal.add(valorTotalItem);
                    } else {
                        // Estoque insuficiente, cancela a transação
                        throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
                    }
                }
            }
            
            if (!itensVenda.isEmpty()) {
                venda.setItens(itensVenda);
                venda.setValorTotal(valorTotal);
                return vendaRepository.save(venda);
            }
        }
        
        return null;
    }
}