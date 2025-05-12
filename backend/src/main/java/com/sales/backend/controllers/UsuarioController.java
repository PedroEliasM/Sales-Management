package com.sales.backend.controllers;

import com.sales.backend.dto.UsuarioDTO;
import com.sales.backend.models.Usuario;
import com.sales.backend.services.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;
//import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarioService.converterParaDTO(usuarios));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<UsuarioDTO>> listarFuncionarios() {
        List<Usuario> funcionarios = usuarioService.listarPorTipo(Usuario.TipoUsuario.funcionario);
        return ResponseEntity.ok(usuarioService.converterParaDTO(funcionarios));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuarioService.converterParaDTO(usuario));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<UsuarioDTO> cadastrarFuncionario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        if (!usuarioDTO.getTipoUsuario().equals(Usuario.TipoUsuario.funcionario)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Usuario novoUsuario = usuarioService.salvar(usuarioDTO);
        return new ResponseEntity<>(usuarioService.converterParaDTO(novoUsuario), HttpStatus.CREATED);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<UsuarioDTO> atualizarFuncionario(@PathVariable Integer id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        if (!usuarioDTO.getTipoUsuario().equals(Usuario.TipoUsuario.funcionario)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuarioDTO);
        return ResponseEntity.ok(usuarioService.converterParaDTO(usuarioAtualizado));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Void> excluirFuncionario(@PathVariable Integer id) {
        usuarioService.excluir(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}