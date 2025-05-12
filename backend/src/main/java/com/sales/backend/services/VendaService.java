package com.sales.backend.services;

import com.sales.backend.dto.VendaDTO;
import com.sales.backend.exception.ResourceNotFoundException;
import com.sales.backend.models.ItemVenda;
import com.sales.backend.models.Produto;
import com.sales.backend.models.Usuario;
import com.sales.backend.models.Venda;
import com.sales.backend.repositories.ItemVendaRepository;
import com.sales.backend.repositories.ProdutoRepository;
import com.sales.backend.repositories.UsuarioRepository;
import com.sales.backend.repositories.VendaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ItemVendaRepository itemVendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueService estoqueService;

    @Transactional(readOnly = true)
    public List<Venda> listarTodos() {
        return vendaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Venda buscarPorId(Integer id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com ID: " + id));
    }

    @Transactional
    public Venda salvar(VendaDTO vendaDTO) {
        Usuario cliente = usuarioRepository.findById(vendaDTO.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setDataVenda(LocalDateTime.now());
        venda.setValorTotal(BigDecimal.ZERO); // Inicialmente zero, será calculado abaixo

        Venda vendaSalva = vendaRepository.save(venda);

        final BigDecimal[] valorTotalVenda = {BigDecimal.ZERO}; // Usando um array mutável
        
        List<ItemVenda> itensVenda = vendaDTO.getItens().stream().map(itemDTO -> {
            Produto produto = produtoRepository.findById(itemDTO.getIdProduto())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + itemDTO.getIdProduto()));

            // Verificar estoque e reduzir
            estoqueService.reduzirEstoque(produto.getId(), itemDTO.getQuantidade());

            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setVenda(vendaSalva);
            itemVenda.setProduto(produto);
            itemVenda.setQuantidadeVendida(itemDTO.getQuantidade());
            itemVenda.setValorUnitario(produto.getValor());
            itemVenda.setValorTotal(produto.getValor().multiply(BigDecimal.valueOf(itemDTO.getQuantidade())));

            valorTotalVenda[0] = valorTotalVenda[0].add(itemVenda.getValorTotal());
            return itemVendaRepository.save(itemVenda);
        }).collect(Collectors.toList());

        vendaSalva.setItens(itensVenda);
        vendaSalva.setValorTotal(valorTotalVenda[0]); // Atribuindo o valor total calculado

        return vendaRepository.save(vendaSalva);
    }

    @Transactional
    public void excluir(Integer id) {
        Venda venda = buscarPorId(id);
        // Excluir os itens da venda primeiro (a integridade referencial cuidará disso se onDelete CASCADE estiver configurado no banco)
        vendaRepository.delete(venda);
    }

    public List<VendaDTO> converterParaDTO(List<Venda> vendas) {
        return vendas.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }

    public VendaDTO converterParaDTO(Venda venda) {
        VendaDTO dto = new VendaDTO();
        dto.setId(venda.getId());
        dto.setIdCliente(venda.getCliente().getId());
        dto.setValorTotal(venda.getValorTotal());
        dto.setItens(venda.getItens().stream().map(item -> {
            VendaDTO.ItemVendaDTO itemDTO = new VendaDTO.ItemVendaDTO();
            itemDTO.setId(item.getId());
            itemDTO.setIdProduto(item.getProduto().getId());
            itemDTO.setQuantidade(item.getQuantidadeVendida());
            itemDTO.setValorUnitario(item.getValorUnitario());
            itemDTO.setValorTotal(item.getValorTotal());
            itemDTO.setNomeProduto(item.getProduto().getNome());
            return itemDTO;
        }).collect(Collectors.toList()));
        return dto;
    }
}