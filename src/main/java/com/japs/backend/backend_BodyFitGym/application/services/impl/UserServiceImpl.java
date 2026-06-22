package com.japs.backend.backend_BodyFitGym.application.services.impl;

import com.japs.backend.backend_BodyFitGym.application.dto.UserSearchCriteria;
import com.japs.backend.backend_BodyFitGym.application.services.UserService;
import com.japs.backend.backend_BodyFitGym.domain.model.User;
import com.japs.backend.backend_BodyFitGym.domain.port.in.user.ICreateUserUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.user.IDeleteUserUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.in.user.IRetrieveUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final ICreateUserUseCase iCreateUserUseCase;
    private final IDeleteUserUseCase iDeleteUserUseCase;
    private final IRetrieveUserUseCase iRetrieveUserUseCase;
    //private final IUpdateUserUseCase iUpdateUserUseCase;

    @Override
    public User save(User user) {
        return iCreateUserUseCase.createUser(user);
    }

    @Override
    public void delete(Long id) {
        iDeleteUserUseCase.deleteUser(id);
    }

    @Override
    public User findByUserName(String userName) {
        return iRetrieveUserUseCase.getUserByUserName(userName);
    }

    @Override
    public User findByDocument(String document) {
        return iRetrieveUserUseCase.getUserByDocument(document);
    }

    @Override
    public User findById(Long id) {
        return iRetrieveUserUseCase.getUserById(id);
    }

    @Override
    public List<User> search(UserSearchCriteria userSearchCriteria) {
        return iRetrieveUserUseCase.search(userSearchCriteria);
    }
}
