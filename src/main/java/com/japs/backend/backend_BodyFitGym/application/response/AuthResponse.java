package com.japs.backend.backend_BodyFitGym.application.response;

import com.japs.backend.backend_BodyFitGym.domain.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private Long id;
    private String token;
    private String userName;
    private String name;
    private String lastName;
    private String document;
    private Role role;
    private Integer expiresIn;
}
