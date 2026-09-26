package com.japs.backend.backend_BodyFitGym.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateMembership {

    private Long id;

    @NotNull(message = "El afiliado es obligatorio.")
    private Long affiliateId;

    @NotNull(message = "La membresía es obligatoria.")
    private Long membershipId;

    private LocalDate startDate;

    private LocalDate endDate;

    private TrackingMode trackingMode;

    private Integer remainingUnits;

    private SubscriptionStatus status;

    private Boolean frozen;

    private LocalDate frozenAt;

    private LocalDate createdAt;
}
