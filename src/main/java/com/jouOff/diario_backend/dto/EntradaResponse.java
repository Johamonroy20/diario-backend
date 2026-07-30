package com.jouOff.diario_backend.dto;

import com.jouOff.diario_backend.enums.EstadoAnimo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor

public class EntradaResponse {

    private Long id;
    private String titulo;
    private String contenido;
    private EstadoAnimo estadoAnimo;
    private LocalDateTime fecha;

}
