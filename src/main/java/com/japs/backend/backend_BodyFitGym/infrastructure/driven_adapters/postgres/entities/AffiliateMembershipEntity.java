package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities;

import com.japs.backend.backend_BodyFitGym.domain.model.SubscriptionStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
@Entity(name = "affiliate_membership")
public class AffiliateMembershipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Long simple (sin @ManyToOne/@JoinColumn ni FK a nivel de BD) a propósito:
    // igual que Affiliate/Membership, para que la migración de datos legados
    // no dependa del orden de carga ni de referencias íntegras desde el día uno.
    @Column(name = "affiliate_id")
    private Long affiliateId;

    @Column(name = "membership_id")
    private Long membershipId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "tracking_mode")
    private TrackingMode trackingMode;

    @Column(name = "remaining_units")
    private Integer remainingUnits;

    @Enumerated(EnumType.STRING)
    @Column
    private SubscriptionStatus status;

    @Column
    private Boolean frozen;

    @Column(name = "frozen_at")
    private LocalDate frozenAt;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = SubscriptionStatus.ACTIVA;
        }
        if (this.frozen == null) {
            this.frozen = false;
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDate.now();
        }
    }
}
