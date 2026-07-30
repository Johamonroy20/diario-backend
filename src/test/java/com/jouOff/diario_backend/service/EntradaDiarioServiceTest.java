package com.jouOff.diario_backend.service;


import com.jouOff.diario_backend.dto.CrearEntradaRequest;
import com.jouOff.diario_backend.enums.EstadoAnimo;
import com.jouOff.diario_backend.model.EntradaDiario;
import com.jouOff.diario_backend.model.Usuario;
import com.jouOff.diario_backend.repository.EntradaDiarioRepository;
import com.jouOff.diario_backend.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class EntradaDiarioServiceTest {

     @Mock
    private EntradaDiarioRepository entradaDiarioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EntradaDiarioService entradaDiarioService;

    private Usuario usuarioExistente;
    private CrearEntradaRequest request;

    @BeforeEach
    void setUp() {
        usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setEmail("test@test.com");
        usuarioExistente.setNombreCompleto("Test Usuario");

        request = new CrearEntradaRequest();
        request.setTitulo("Mi entrada");
        request.setContenido("Contenido de prueba");
        request.setEstadoAnimo(EstadoAnimo.FELIZ);

        var authentication = new UsernamePasswordAuthenticationToken(1L, null, Collections.emptyList());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void crear_conDatosValidos_creaEntradaCorrectamente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));

        EntradaDiario entradaGuardada = new EntradaDiario();
        entradaGuardada.setId(10L);
        entradaGuardada.setTitulo("Mi entrada");
        entradaGuardada.setContenido("Contenido de prueba");
        entradaGuardada.setEstadoAnimo(EstadoAnimo.FELIZ);
        entradaGuardada.setUsuario(usuarioExistente);

        when(entradaDiarioRepository.save(org.mockito.ArgumentMatchers.any(EntradaDiario.class)))
                .thenReturn(entradaGuardada);

        var resultado = entradaDiarioService.crear(request);

        assertThat(resultado.getId()).isEqualTo(10L);
        assertThat(resultado.getTitulo()).isEqualTo("Mi entrada");
        assertThat(resultado.getEstadoAnimo()).isEqualTo(EstadoAnimo.FELIZ);
    }

    @Test
    void crear_conUsuarioInexistente_lanzaExcepcion() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> entradaDiarioService.crear(request)
        );
    }

}
