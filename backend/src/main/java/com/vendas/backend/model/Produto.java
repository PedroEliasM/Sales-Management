package com.vendas.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "produto")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Integer id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private BigDecimal valor;
    
    private String descricao;
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
    
    @ManyToOne
    @JoinColumn(name = "id_funcionario_cadastro", nullable = false)
    private Usuario funcionarioCadastro;
    
    @PrePersist
    public void prePersist() {
        dataCadastro = LocalDateTime.now();
    }
}