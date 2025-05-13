package com.vendas.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "item_venda")
public class ItemVenda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_venda")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "id_venda", nullable = false)
    private Venda venda;
    
    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;
    
    @Column(name = "quantidade_vendida", nullable = false)
    private Integer quantidadeVendida;
    
    @Column(name = "valor_unitario_na_venda", nullable = false)
    private BigDecimal valorUnitarioNaVenda;
    
    @Column(name = "valor_total_item", nullable = false)
    private BigDecimal valorTotalItem;
    
    @PrePersist
    @PreUpdate
    public void calcularValorTotal() {
        if (quantidadeVendida != null && valorUnitarioNaVenda != null) {
            valorTotalItem = valorUnitarioNaVenda.multiply(BigDecimal.valueOf(quantidadeVendida));
        }
    }
}