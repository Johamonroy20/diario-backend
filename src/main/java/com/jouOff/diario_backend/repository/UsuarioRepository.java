package com.jouOff.diario_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jouOff.diario_backend.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);
}
