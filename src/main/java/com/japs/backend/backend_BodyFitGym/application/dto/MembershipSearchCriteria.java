package com.japs.backend.backend_BodyFitGym.application.dto;

import com.japs.backend.backend_BodyFitGym.domain.model.DurationUnit;
import com.japs.backend.backend_BodyFitGym.domain.model.MembershipStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.TrackingMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipSearchCriteria {

    private String name;

    private DurationUnit durationUnit;

    private TrackingMode trackingMode;

    private MembershipStatus status;
}
