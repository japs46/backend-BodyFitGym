package com.japs.backend.backend_BodyFitGym.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    private String userName;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;
}
