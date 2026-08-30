package com.japs.backend.backend_BodyFitGym.application.services;

import com.japs.backend.backend_BodyFitGym.application.dto.UserSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    void delete(Long id);

    User findByUserName(String userName);

    User findByDocument(String document);

    User findById(Long id);

    Page<User> search(UserSearchCriteria userSearchCriteria, Pageable pageable);
}
