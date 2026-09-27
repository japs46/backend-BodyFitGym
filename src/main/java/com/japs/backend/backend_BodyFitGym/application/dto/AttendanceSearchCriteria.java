package com.japs.backend.backend_BodyFitGym.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSearchCriteria {

    private Long affiliateId;

    private LocalDate dateFrom;

    private LocalDate dateTo;
}
