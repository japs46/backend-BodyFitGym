package com.japs.backend.backend_BodyFitGym.domain.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

    private Long id;

    @NotBlank(message = "El nombre de la membresía es obligatorio.")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres.")
    private String name;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres.")
    private String description;

    @NotNull(message = "La unidad de duración es obligatoria.")
    private DurationUnit durationUnit;

    @NotNull(message = "La cantidad de duración es obligatoria.")
    @Positive(message = "La cantidad de duración debe ser un número positivo.")
    private Integer durationQuantity;

    @NotNull(message = "El modo de seguimiento es obligatorio.")
    private TrackingMode trackingMode;

    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0.")
    private BigDecimal price;

    private MembershipStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
