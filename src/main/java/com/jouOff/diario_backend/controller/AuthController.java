package com.jouOff.diario_backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jouOff.diario_backend.dto.RegistroRequest;
import com.jouOff.diario_backend.model.Usuario;
import com.jouOff.diario_backend.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequest request){
    try {
         Usuario usuarioCreado = authService.registrar(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "id", usuarioCreado.getId(),
                    "nombreCompleto", usuarioCreado.getNombreCompleto(),
                    "email", usuarioCreado.getEmail(),
                    "mensaje", "Cuenta creada exitosamente"
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

}
