package com.japs.backend.backend_BodyFitGym.domain.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class Product {

    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio.")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres.")
    private String name;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres.")
    private String description;

    @NotNull(message = "La cantidad en stock es obligatoria.")
    @PositiveOrZero(message = "La cantidad en stock no puede ser negativa.")
    private Integer quantity;

    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0.")
    private BigDecimal price;

    private ProductStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
