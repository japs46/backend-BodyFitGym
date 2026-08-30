package com.japs.backend.backend_BodyFitGym.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteria {

    private String name;

    private String document;

    private String userName;
}
