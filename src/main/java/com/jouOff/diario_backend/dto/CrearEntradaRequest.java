package com.jouOff.diario_backend.dto;

import com.jouOff.diario_backend.enums.EstadoAnimo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class CrearEntradaRequest {

     private String titulo;

    @NotBlank(message = "El contenido no puede estar vacío")
    private String contenido;

    @NotNull(message = "El estado de ánimo es obligatorio")
    private EstadoAnimo estadoAnimo;

}
