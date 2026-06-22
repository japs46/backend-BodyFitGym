package com.japs.backend.backend_BodyFitGym.application.usecases.user;

import com.japs.backend.backend_BodyFitGym.domain.model.User;
import com.japs.backend.backend_BodyFitGym.domain.port.in.user.ICreateUserUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateUserUseCaseImpl implements ICreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {

        User existingUserWithUserName = userRepositoryPort.findByUserName(user.getUserName()).orElse(null);
        if(existingUserWithUserName != null){
            throw new RuntimeException("Ya existe un usuario con este nombre de usuario");
        }

        User existingUserWithDocument = userRepositoryPort.findByDocument(user.getDocument()).orElse(null);
        if(existingUserWithDocument != null){
            throw new RuntimeException("Ya existe un usuario con este documento");
        }

        if(user.getPassword() != null && !user.getPassword().isBlank()){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepositoryPort.save(user);
    }
}
