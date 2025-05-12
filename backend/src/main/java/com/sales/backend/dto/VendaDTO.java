package com.sales.backend.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaDTO {
    
    private Integer id;
    
    @NotNull(message = "ID do cliente é obrigatório")
    private Integer idCliente;
    
    private BigDecimal valorTotal;
    
    @NotEmpty(message = "Itens da venda são obrigatórios")
    private List<ItemVendaDTO> itens;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemVendaDTO {
        
        private Integer id;
        
        @NotNull(message = "ID do produto é obrigatório")
        private Integer idProduto;
        
        @NotNull(message = "Quantidade é obrigatória")
        private Integer quantidade;
        
        private BigDecimal valorUnitario;
        
        private BigDecimal valorTotal;
        
        private String nomeProduto;
    }
}