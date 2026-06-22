package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.adapter;

import com.japs.backend.backend_BodyFitGym.application.dto.UserSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.User;
import com.japs.backend.backend_BodyFitGym.domain.port.out.UserRepositoryPort;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.UserEntity;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.helper.UserSpecification;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper.UserMapper;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserAdapter implements UserRepositoryPort {

    private final UserEntityRepository userEntityRepository;
    @Override
    public User save(User user) {
        UserEntity userEntity = UserMapper.toEntity(user);
        return UserMapper.toModel(userEntityRepository.save(userEntity));
    }

    @Override
    public void delete(Long id) {
       userEntityRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userEntityRepository.findById(id)
                .map(UserMapper::toModel);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return userEntityRepository.findByUserName(userName)
                .map(UserMapper::toModel);
    }

    @Override
    public Optional<User> findByDocument(String document) {
        return userEntityRepository.findByDocument(document)
                .map(UserMapper::toModel);
    }

    @Override
    public List<User> search(UserSearchCriteria userSearchCrtiteria) {

        Specification<UserEntity> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if(userSearchCrtiteria.getUserName() != null){
            specification = specification.and(UserSpecification.userNameStartsWith(userSearchCrtiteria.getUserName()));
        }

        if(userSearchCrtiteria.getName() != null){
            specification = specification.and(UserSpecification.nameStarrsWith(userSearchCrtiteria.getName()));
        }

        if(userSearchCrtiteria.getDocument() != null){
            specification = specification.and(UserSpecification.documentStartsWith(userSearchCrtiteria.getDocument()));
        }
        return userEntityRepository.findAll(specification)
                .stream()
                .map(UserMapper::toModel)
                .toList();
    }
}
