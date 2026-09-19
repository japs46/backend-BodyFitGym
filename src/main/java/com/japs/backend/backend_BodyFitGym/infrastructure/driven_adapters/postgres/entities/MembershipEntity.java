package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities;

import com.japs.backend.backend_BodyFitGym.domain.model.DurationUnit;
import com.japs.backend.backend_BodyFitGym.domain.model.MembershipStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
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
@Entity(name = "membership")
public class MembershipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sin "unique"/"nullable = false" a propósito: la migración de datos del sistema
    // legado (sin validaciones) debe poder cargar filas incompletas o con nombres
    // duplicados sin que la base de datos las rechace. La integridad de lo que
    // entra por la API la garantiza la validación de Membership (Bean Validation)
    // y las reglas de negocio en los casos de uso (ej. unicidad de nombre).
    @Column
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "duration_unit")
    private DurationUnit durationUnit;

    @Column(name = "duration_quantity")
    private Integer durationQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "tracking_mode")
    private TrackingMode trackingMode;

    @Column
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column
    private MembershipStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = MembershipStatus.ACTIVA;
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
