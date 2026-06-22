package com.japs.backend.backend_BodyFitGym.domain.port.out;

import com.japs.backend.backend_BodyFitGym.application.dto.UserSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort{

    User save(User user);

    void delete(Long id);

    Optional<User> findById(Long id);

    Optional<User> findByUserName(String userName);

    Optional<User> findByDocument(String document);

    List<User> search(UserSearchCriteria userSearchCrtiteria);
}
