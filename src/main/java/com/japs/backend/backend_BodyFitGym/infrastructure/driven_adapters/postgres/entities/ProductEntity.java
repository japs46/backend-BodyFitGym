package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities;

import com.japs.backend.backend_BodyFitGym.domain.model.ProductStatus;
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
@Entity(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sin "unique"/"nullable = false" a propósito: la migración de datos del
    // sistema legado (sin validaciones) debe poder cargar filas incompletas o
    // con nombres duplicados sin que la base de datos las rechace.
    @Column
    private String name;

    @Column(length = 500)
    private String description;

    @Column
    private Integer quantity;

    @Column
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column
    private ProductStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = ProductStatus.ACTIVO;
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
