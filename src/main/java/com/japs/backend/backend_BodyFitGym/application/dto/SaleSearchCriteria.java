package com.japs.backend.backend_BodyFitGym.application.dto;

import com.japs.backend.backend_BodyFitGym.domain.model.SaleStatus;
import com.japs.backend.backend_BodyFitGym.domain.model.SaleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleSearchCriteria {

    private Long affiliateId;

    private SaleType type;

    private SaleStatus status;

    private LocalDateTime dateFrom;

    private LocalDateTime dateTo;
}
