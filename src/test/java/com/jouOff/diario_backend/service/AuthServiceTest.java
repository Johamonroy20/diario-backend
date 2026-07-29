package com.jouOff.diario_backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jouOff.diario_backend.dto.LoginRequest;
import com.jouOff.diario_backend.dto.LoginResponse;
import com.jouOff.diario_backend.dto.RegistroRequest;
import com.jouOff.diario_backend.model.Usuario;
import com.jouOff.diario_backend.repository.UsuarioRepository;
import com.jouOff.diario_backend.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

 @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegistroRequest registroRequest;
    private LoginRequest loginRequest;
    private Usuario usuarioExistente;

    @BeforeEach
    void setUp() {
        registroRequest = new RegistroRequest();
        registroRequest.setNombreCompleto("Test Usuario");
        registroRequest.setEmail("test@test.com");
        registroRequest.setPassword("12345678");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("12345678");

        usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNombreCompleto("Test Usuario");
        usuarioExistente.setEmail("test@test.com");
        usuarioExistente.setPassword("hashEncriptado");
    }

    @Test
    void registrar_conEmailNuevo_creaUsuarioCorrectamente() {
        when(usuarioRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("12345678")).thenReturn("hashEncriptado");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

        Usuario resultado = authService.registrar(registroRequest);

        assertThat(resultado.getEmail()).isEqualTo("test@test.com");
        assertThat(resultado.getPassword()).isEqualTo("hashEncriptado");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrar_conEmailDuplicado_lanzaExcepcion() {
        when(usuarioRepository.existsByEmail("test@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.registrar(registroRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El email ya está registrado");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void login_conCredencialesValidas_devuelveTokenYDatos() {
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuarioExistente));
        when(passwordEncoder.matches("12345678", "hashEncriptado")).thenReturn(true);
        when(jwtUtil.generarToken(1L, "test@test.com")).thenReturn("token-simulado");

        LoginResponse resultado = authService.login(loginRequest);

        assertThat(resultado.getToken()).isEqualTo("token-simulado");
        assertThat(resultado.getEmail()).isEqualTo("test@test.com");
        assertThat(resultado.getNombreCompleto()).isEqualTo("Test Usuario");
    }

    @Test
    void login_conEmailInexistente_lanzaExcepcion() {
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Credenciales inválidas");
    }

    @Test
    void login_conPasswordIncorrecta_lanzaExcepcion() {
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuarioExistente));
        when(passwordEncoder.matches("12345678", "hashEncriptado")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Credenciales inválidas");
    }

}
