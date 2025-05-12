package com.sales.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sales.backend.dto.UsuarioDTO;
import com.sales.backend.exception.ResourceNotFoundException;
import com.sales.backend.models.Usuario;
import com.sales.backend.models.Usuario.TipoUsuario;
import com.sales.backend.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarPorTipo(TipoUsuario tipoUsuario) {
        return usuarioRepository.findByTipoUsuario(tipoUsuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Transactional
    public Usuario salvar(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setSobrenome(usuarioDTO.getSobrenome());
        usuario.setIdade(usuarioDTO.getIdade());
        usuario.setCidade(usuarioDTO.getCidade());
        usuario.setTipoUsuario(usuarioDTO.getTipoUsuario());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setTelefone(usuarioDTO.getTelefone());
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        usuario.setDataCadastro(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Integer id, UsuarioDTO usuarioDTO) {
        Usuario usuario = buscarPorId(id);

        // Verifica se o email já existe e não pertence a este usuário
        if (!usuario.getEmail().equals(usuarioDTO.getEmail()) && 
            usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado para outro usuário");
        }

        usuario.setNome(usuarioDTO.getNome());
        usuario.setSobrenome(usuarioDTO.getSobrenome());
        usuario.setIdade(usuarioDTO.getIdade());
        usuario.setCidade(usuarioDTO.getCidade());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setTelefone(usuarioDTO.getTelefone());

        // Só atualiza a senha se ela for fornecida
        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        }

        // Apenas admin pode mudar o tipo de usuário
        if (usuarioDTO.getTipoUsuario() != null) {
            usuario.setTipoUsuario(usuarioDTO.getTipoUsuario());
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluir(Integer id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }

    public List<UsuarioDTO> converterParaDTO(List<Usuario> usuarios) {
        return usuarios.stream().map(this::converterParaDTO).collect(Collectors.toList());
    }

    public UsuarioDTO converterParaDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setSobrenome(usuario.getSobrenome());
        dto.setIdade(usuario.getIdade());
        dto.setCidade(usuario.getCidade());
        dto.setTipoUsuario(usuario.getTipoUsuario());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());
        // Não enviar a senha no DTO por segurança
        return dto;
    }
}