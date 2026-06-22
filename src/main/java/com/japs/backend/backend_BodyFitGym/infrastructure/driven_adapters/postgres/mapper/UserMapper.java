package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.User;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.UserEntity;

public class UserMapper {
    public static UserEntity toEntity(User user){

        if(user == null)
            return null;

        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .document(user.getDocument())
                .userName(user.getUserName())
                .password(user.getPassword())
                .status(user.getStatus())
                .registrationDate(user.getRegistrationDate())
                .build();
    }

    public static User toModel(UserEntity userEntity){

        if(userEntity == null)
            return null;

        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .document(userEntity.getDocument())
                .lastName(userEntity.getLastName())
                .userName(userEntity.getUserName())
                .password(userEntity.getPassword())
                .status(userEntity.getStatus())
                .registrationDate(userEntity.getRegistrationDate())
                .build();
    }
}
