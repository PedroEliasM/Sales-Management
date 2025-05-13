package com.vendas.backend.dto;

import com.vendas.backend.model.Usuario.TipoUsuario;
import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer id;
    private String nome;
    private String sobrenome;
    private Integer idade;
    private String cidade;
    private TipoUsuario tipoUsuario;
    private String email;
    private String telefone;
    private String senha;
}