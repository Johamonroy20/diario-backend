package com.jouOff.diario_backend.service;

import com.jouOff.diario_backend.dto.CrearEntradaRequest;
import com.jouOff.diario_backend.dto.EntradaResponse;
import com.jouOff.diario_backend.model.EntradaDiario;
import com.jouOff.diario_backend.model.Usuario;
import com.jouOff.diario_backend.repository.EntradaDiarioRepository;
import com.jouOff.diario_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class EntradaDiarioService {

    private final EntradaDiarioRepository entradaDiarioRepository;
    private final UsuarioRepository usuarioRepository;

    public EntradaResponse crear(CrearEntradaRequest request) {
        Long usuarioId = obtenerUsuarioAutenticadoId();

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        EntradaDiario entrada = new EntradaDiario();
        entrada.setTitulo(request.getTitulo());
        entrada.setContenido(request.getContenido());
        entrada.setEstadoAnimo(request.getEstadoAnimo());
        entrada.setFecha(LocalDateTime.now());
        entrada.setUsuario(usuario);

        EntradaDiario entradaGuardada = entradaDiarioRepository.save(entrada);

        return new EntradaResponse(
                entradaGuardada.getId(),
                entradaGuardada.getTitulo(),
                entradaGuardada.getContenido(),
                entradaGuardada.getEstadoAnimo(),
                entradaGuardada.getFecha());
    }
    private Long obtenerUsuarioAutenticadoId() {
    return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
}

}
