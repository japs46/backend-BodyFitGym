package com.japs.backend.backend_BodyFitGym.domain.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class Affiliate {

    private Long id;

    @NotBlank(message = "La identificación es obligatoria.")
    @Size(max = 20, message = "La identificación no puede superar los 20 caracteres.")
    private String identification;

    @NotBlank(message = "El primer nombre es obligatorio.")
    @Size(max = 255, message = "El primer nombre no puede superar los 255 caracteres.")
    private String firstName;

    @Size(max = 255, message = "El segundo nombre no puede superar los 255 caracteres.")
    private String middleName;

    @NotBlank(message = "El primer apellido es obligatorio.")
    @Size(max = 255, message = "El primer apellido no puede superar los 255 caracteres.")
    private String lastName;

    @Size(max = 255, message = "El segundo apellido no puede superar los 255 caracteres.")
    private String secondLastName;

    @Size(max = 20, message = "El sexo no puede superar los 20 caracteres.")
    private String sex;

    private LocalDate birthDate;

    private LocalDate affiliationDate;

    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres.")
    private String address;

    @Size(max = 255, message = "El barrio no puede superar los 255 caracteres.")
    private String neighborhood;

    @Size(max = 255, message = "La ciudad no puede superar los 255 caracteres.")
    private String city;

    private String status;

    @Size(max = 20, message = "El código postal no puede superar los 20 caracteres.")
    private String postalCode;

    @Size(max = 20, message = "El teléfono de casa no puede superar los 20 caracteres.")
    private String homePhone;

    @Size(max = 20, message = "El teléfono celular no puede superar los 20 caracteres.")
    private String mobilePhone;

    @Email(message = "El correo electrónico no tiene un formato válido.")
    @Size(max = 255, message = "El correo electrónico no puede superar los 255 caracteres.")
    private String email;

    @Size(max = 255, message = "La ocupación no puede superar los 255 caracteres.")
    private String occupation;

    private Double weight;

    private Double height;

    private Double waist;

    private Double leg;

    private Double hip;
}
