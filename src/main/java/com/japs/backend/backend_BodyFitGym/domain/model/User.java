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

    @NotBlank(message = "La Cedula no puede estar vacia.")
    @Pattern(regexp = "\\d+", message = "La cedula solo debe contener números.")
    @Size(min = 6, max = 11, message = "La cédula debe tener entre 6 y 11 digitos.")
    private String document;

    @NotBlank(message = "El Nombre no puede estar Vacio")
    @Size(max = 255, message = "El Nombre supera la cantidad de caracteres permitidos")
    private String name;

    @NotBlank(message = "El Apellido no puede estar vacio")
    @Size(max = 255, message = "El Apellido supera la cantidad de caracteres permitidos")
    private String lastName;

    @NotBlank(message = "El Nombre de Usuario no puede estar vacio")
    @Size(max = 255, message = "El Nombre de Usuario supera la cantidad de caracteres permitidos")
    private String userName;

    @NotBlank(message = "La Contraseña no puede estar vacia")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#_-])[A-Za-z\\d@$!%*?&.#_-]{8,}$",
        message = "Contraseña inválida. Ejemplo de formato válido: MiClave123!"
    )
    private String password;

    private String status;

    private LocalDate registrationDate;

    //private List<AfiliadoSimple> afiliados;
    //private List<ProductoSimple> productos;
    //private List<MembresiaSimple> membresias;

}
