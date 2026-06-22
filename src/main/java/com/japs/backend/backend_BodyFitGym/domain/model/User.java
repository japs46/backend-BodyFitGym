package com.japs.backend.backend_BodyFitGym.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    @NotBlank(message = "El número de documento es obligatorio.")
    @Pattern(regexp = "\\d+", message = "El documento solo debe contener dígitos.")
    @Size(min = 6, max = 11, message = "El documento debe tener entre 6 y 11 dígitos.")
    private String document;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres.")
    private String name;

    @NotBlank(message = "El apellido es obligatorio.")
    @Size(max = 255, message = "El apellido no puede superar los 255 caracteres.")
    private String lastName;

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(max = 255, message = "El nombre de usuario no puede superar los 255 caracteres.")
    private String userName;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#_-])[A-Za-z\\d@$!%*?&.#_-]{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres e incluir: una mayúscula, una minúscula, un número y un carácter especial (@$!%*?&.#_-)."
    )
    private String password;

    private String status;

    private LocalDate registrationDate;

    //private List<AfiliadoSimple> afiliados;
    //private List<ProductoSimple> productos;
    //private List<MembresiaSimple> membresias;

}
