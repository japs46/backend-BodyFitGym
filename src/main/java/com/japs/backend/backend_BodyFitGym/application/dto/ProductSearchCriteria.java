package com.japs.backend.backend_BodyFitGym.application.dto;

import com.japs.backend.backend_BodyFitGym.domain.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchCriteria {

    private String name;

    private ProductStatus status;
}
