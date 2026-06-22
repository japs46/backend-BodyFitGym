package com.japs.backend.backend_BodyFitGym.application.services;

import com.japs.backend.backend_BodyFitGym.application.dto.UserSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    void delete(Long id);

    User findByUserName(String userName);

    User findByDocument(String document);

    User findById(Long id);

    List<User> search(UserSearchCriteria userSearchCriteria);
}
