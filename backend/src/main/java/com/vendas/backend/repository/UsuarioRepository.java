package com.vendas.backend.repository;

import com.vendas.backend.model.Usuario;
import com.vendas.backend.model.Usuario.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);
    boolean existsByEmail(String email);
}