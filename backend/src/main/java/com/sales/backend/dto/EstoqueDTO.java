package com.sales.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueDTO {
    
    private Integer id;
    
    @NotNull(message = "ID do produto é obrigatório")
    private Integer idProduto;
    
    @NotNull(message = "Quantidade é obrigatória")
    @PositiveOrZero(message = "Quantidade deve ser zero ou positiva")
    private Integer quantidade;
}