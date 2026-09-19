package com.japs.backend.backend_BodyFitGym.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateSearchCriteria {

    private String identification;

    private String name;

    private String status;
}
