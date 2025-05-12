package com.sales.backend.controllers;

import com.sales.backend.dto.AuthRequest;
import com.sales.backend.dto.AuthResponse;
import com.sales.backend.dto.UsuarioDTO;
import com.sales.backend.models.Usuario;
import com.sales.backend.security.JwtTokenProvider;
import com.sales.backend.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getSenha()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        Usuario usuario = (Usuario) authentication.getPrincipal();

        return ResponseEntity.ok(AuthResponse.builder()
                .token(jwt)
                .tipoUsuario(usuario.getTipoUsuario().name())
                .userId(usuario.getId())
                .nome(usuario.getNome())
                .build());
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Usuario usuario) {
        if (usuarioService.existsByEmail(usuario.getEmail())) {
            return new ResponseEntity<>("Email já cadastrado!", HttpStatus.BAD_REQUEST);
        }
        usuarioService.salvar(new UsuarioDTO(
                null,
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getIdade(),
                usuario.getCidade(),
                Usuario.TipoUsuario.cliente, // Por padrão, o cadastro é para cliente
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getSenha()
        ));
        return new ResponseEntity<>("Usuário registrado com sucesso", HttpStatus.CREATED);
    }
}