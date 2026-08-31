package com.japs.backend.backend_BodyFitGym.domain.port.in.auth;

import com.japs.backend.backend_BodyFitGym.application.response.AuthResponse;
import com.japs.backend.backend_BodyFitGym.domain.model.User;

public interface IAuthUseCase {

    AuthResponse authenticate(String userName, String password);

    User getAuthenticatedUser();
}
