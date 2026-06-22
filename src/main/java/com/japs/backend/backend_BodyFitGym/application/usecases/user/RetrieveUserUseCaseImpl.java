package com.japs.backend.backend_BodyFitGym.application.usecases.user;

import com.japs.backend.backend_BodyFitGym.application.dto.UserSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.User;
import com.japs.backend.backend_BodyFitGym.domain.port.in.user.IRetrieveUserUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class RetrieveUserUseCaseImpl implements IRetrieveUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public User getUserById(Long id) {
        if(id == null){
            throw new IllegalArgumentException("El ID del usuario es obligatorio.");
        }

        if(id <= 0){
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo.");
        }
        return userRepositoryPort.findById(id).orElseThrow(()-> new NoSuchElementException("No existe ningún usuario con el ID: " + id));
    }

    @Override
    public User getUserByDocument(String document) {

        if(document == null || document.isBlank()){
            throw new IllegalArgumentException("El número de documento es obligatorio.");
        }

        if(!document.matches("\\d+")){
            throw new IllegalArgumentException("El número de documento solo debe contener dígitos.");
        }

        return userRepositoryPort.findByDocument(document).orElseThrow(() -> new NoSuchElementException("No existe ningún usuario con el documento: " + document));
    }

    @Override
    public User getUserByUserName(String userName) {

        if(userName == null || userName.isBlank()){
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }

        return userRepositoryPort.findByUserName(userName).orElseThrow(() -> new NoSuchElementException("No existe ningún usuario con el nombre de usuario: " + userName));
    }

    @Override
    public List<User> search(UserSearchCriteria userSearchCriteria) {

        return userRepositoryPort.search(userSearchCriteria);

    }
}
