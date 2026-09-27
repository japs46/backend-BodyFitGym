package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities;

import com.japs.backend.backend_BodyFitGym.domain.model.SaleStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
@Entity(name = "sale")
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sin FK/"nullable = false" a propósito: la migración de datos del sistema
    // legado (sin validaciones) debe poder cargar filas incompletas.
    @Column(name = "affiliate_id")
    private Long affiliateId;

    @Enumerated(EnumType.STRING)
    @Column
    private SaleType type;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "affiliate_membership_id")
    private Long affiliateMembershipId;

    @Column
    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column
    private BigDecimal total;

    @Column(length = 500)
    private String observation;

    @Enumerated(EnumType.STRING)
    @Column
    private SaleStatus status;

    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
