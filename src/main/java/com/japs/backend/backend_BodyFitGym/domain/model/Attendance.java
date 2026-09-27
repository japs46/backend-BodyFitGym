package com.japs.backend.backend_BodyFitGym.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

    private Long id;

    @NotNull(message = "El afiliado es obligatorio.")
    private Long affiliateId;

    private Long affiliateMembershipId;

    private LocalDateTime attendanceDate;
}
