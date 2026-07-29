package com.jouOff.diario_backend.controller;

import com.jouOff.diario_backend.dto.CrearEntradaRequest;
import com.jouOff.diario_backend.dto.EntradaResponse;
import com.jouOff.diario_backend.service.EntradaDiarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entradas")
@RequiredArgsConstructor
public class EntradaDiarioController {

    private final EntradaDiarioService entradaDiarioService;

    @PostMapping
    public ResponseEntity<EntradaResponse> crear(@Valid @RequestBody CrearEntradaRequest request) {
        EntradaResponse entradaCreada = entradaDiarioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(entradaCreada);
    }
}
