package com.vendas.backend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoDTO {
    private Integer id;
    private String nome;
    private BigDecimal valor;
    private String descricao;
    private Integer idFuncionario;
    private Integer quantidade;
}