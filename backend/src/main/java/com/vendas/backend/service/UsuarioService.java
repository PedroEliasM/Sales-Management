package com.vendas.backend.service;

import com.vendas.backend.dto.LoginDTO;
import com.vendas.backend.dto.UsuarioDTO;
import com.vendas.backend.model.Usuario;
import com.vendas.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioDTO> listarFuncionarios() {
        return usuarioRepository.findByTipoUsuario(Usuario.TipoUsuario.funcionario).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {
        Usuario usuario = convertToEntity(usuarioDTO);
        
        // Criptografa a senha usando MD5
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuario.setSenha(getMd5Hash(usuario.getSenha()));
        }
        
        usuario = usuarioRepository.save(usuario);
        return convertToDTO(usuario);
    }

    public UsuarioDTO atualizar(Integer id, UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            
            usuario.setNome(usuarioDTO.getNome());
            usuario.setSobrenome(usuarioDTO.getSobrenome());
            usuario.setIdade(usuarioDTO.getIdade());
            usuario.setCidade(usuarioDTO.getCidade());
            usuario.setTipoUsuario(usuarioDTO.getTipoUsuario());
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setTelefone(usuarioDTO.getTelefone());
            
            // Atualiza a senha apenas se uma nova senha for fornecida
            if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
                usuario.setSenha(getMd5Hash(usuarioDTO.getSenha()));
            }
            
            usuario = usuarioRepository.save(usuario);
            return convertToDTO(usuario);
        }
        
        return null;
    }

    public boolean excluir(Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public UsuarioDTO login(LoginDTO loginDTO) {
        if (loginDTO.getEmail() == null || loginDTO.getSenha() == null) {
            return null;
        }

        String senhaMd5 = getMd5Hash(loginDTO.getSenha());
        
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(loginDTO.getEmail());
        
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (usuario.getSenha().equals(senhaMd5)) {
                return convertToDTO(usuario);
            }
        }
        
        return null;
    }

    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setSobrenome(usuario.getSobrenome());
        dto.setIdade(usuario.getIdade());
        dto.setCidade(usuario.getCidade());
        dto.setTipoUsuario(usuario.getTipoUsuario());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());
        // Não transferir a senha para o DTO por segurança
        return dto;
    }

    private Usuario convertToEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNome(dto.getNome());
        usuario.setSobrenome(dto.getSobrenome());
        usuario.setIdade(dto.getIdade());
        usuario.setCidade(dto.getCidade());
        usuario.setTipoUsuario(dto.getTipoUsuario());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefone(dto.getTelefone());
        usuario.setSenha(dto.getSenha());
        return usuario;
    }

    private String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}