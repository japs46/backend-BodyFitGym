package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
@Entity(name = "attendance")
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sin FK a nivel de BD (mismo criterio que Affiliate/Membership/AffiliateMembership)
    // para no bloquear una eventual migración de datos legados.
    @Column(name = "affiliate_id")
    private Long affiliateId;

    @Column(name = "affiliate_membership_id")
    private Long affiliateMembershipId;

    @Column(name = "attendance_date")
    private LocalDateTime attendanceDate;

    @PrePersist
    protected void onCreate() {
        if (this.attendanceDate == null) {
            this.attendanceDate = LocalDateTime.now();
        }
    }
}
