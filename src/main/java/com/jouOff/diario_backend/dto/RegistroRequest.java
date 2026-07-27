package com.jouOff.diario_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombreCompleto;

    @NotBlank(message = "El email es obligatorio")
    @Email (message = "El email debe tener un formato válido")
    private String email;

    @notBlank (message = "La contraseña es obligatoria")
    @Size (min = 8, message = "La contraseña debe tene almenos 8 caracteres")
    private String password;

    public RegistroRequest(){

    }

    public String getNombreCompleto () {
        return nombreCompleto;
    }

    public void SetNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
