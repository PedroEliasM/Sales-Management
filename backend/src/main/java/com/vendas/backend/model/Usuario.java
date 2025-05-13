package com.vendas.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String sobrenome;
    
    @Column(nullable = false)
    private Integer idade;
    
    @Column(nullable = false)
    private String cidade;
    
    @Column(name = "tipo_usuario", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;
    
    @Column(unique = true)
    private String email;
    
    private String telefone;
    
    private String senha;
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
    
    @PrePersist
    public void prePersist() {
        dataCadastro = LocalDateTime.now();
    }
    
    public enum TipoUsuario {
        admin,
        funcionario,
        cliente
    }
}