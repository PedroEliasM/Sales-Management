package com.vendas.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class VendaDTO {
    private Integer idCliente;
    private List<ItemVendaDTO> itens;
    
    @Data
    public static class ItemVendaDTO {
        private Integer idProduto;
        private Integer quantidade;
        private BigDecimal valorUnitario;
    }
}