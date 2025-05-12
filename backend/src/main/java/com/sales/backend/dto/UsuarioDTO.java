package com.sales.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.sales.backend.models.Usuario.TipoUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    
    private Integer id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotBlank(message = "Sobrenome é obrigatório")
    private String sobrenome;
    
    @NotNull(message = "Idade é obrigatória")
    @Min(value = 18, message = "Idade deve ser maior que 18")
    private Integer idade;
    
    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;
    
    @NotNull(message = "Tipo de usuário é obrigatório")
    private TipoUsuario tipoUsuario;
    
    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;
    
    private String telefone;
    
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
}