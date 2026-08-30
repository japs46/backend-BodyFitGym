package com.japs.backend.backend_BodyFitGym.application.usecases.user;

import com.japs.backend.backend_BodyFitGym.domain.model.User;
import com.japs.backend.backend_BodyFitGym.domain.port.in.user.IDeleteUserUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class DeleteUserUseCaseImpl implements IDeleteUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void deleteUser(Long id) {

        User existingUserWithId = userRepositoryPort.findById(id).orElse(null);
        if(existingUserWithId == null){
            throw new NoSuchElementException("No existe ningún usuario con el ID: " + id);
        }

        userRepositoryPort.delete(id);
    }
}
