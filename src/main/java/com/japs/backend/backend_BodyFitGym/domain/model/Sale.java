package com.japs.backend.backend_BodyFitGym.domain.model;

import jakarta.validation.constraints.NotNull;
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
public class Sale {

    private Long id;

    @NotNull(message = "El afiliado es obligatorio.")
    private Long affiliateId;

    @NotNull(message = "El tipo de venta es obligatorio.")
    private SaleType type;

    private Long productId;

    private Long affiliateMembershipId;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal total;

    @Size(max = 500, message = "La observación no puede superar los 500 caracteres.")
    private String observation;

    private SaleStatus status;

    private LocalDateTime saleDate;

    private LocalDateTime createdAt;
}
