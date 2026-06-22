package com.japs.backend.backend_BodyFitGym.domain.port.in.user;

import com.japs.backend.backend_BodyFitGym.application.dto.UserSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.User;
import java.util.List;

public interface IRetrieveUserUseCase {

    User getUserById(Long id);

    User getUserByDocument(String document);

    User getUserByUserName(String userName);

    List<User> search(UserSearchCriteria userSearchCriteria);
}
