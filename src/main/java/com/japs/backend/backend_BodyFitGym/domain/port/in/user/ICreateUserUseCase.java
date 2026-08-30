package com.japs.backend.backend_BodyFitGym.domain.port.in.user;

import com.japs.backend.backend_BodyFitGym.domain.model.User;

public interface ICreateUserUseCase {

    User createUser(User user);
}
