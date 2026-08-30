package com.japs.backend.backend_BodyFitGym.domain.port.in.user;

import com.japs.backend.backend_BodyFitGym.application.dto.UserSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRetrieveUserUseCase {

    User getUserById(Long id);

    User getUserByDocument(String document);

    User getUserByUserName(String userName);

    Page<User> search(UserSearchCriteria userSearchCriteria, Pageable pageable);
}
