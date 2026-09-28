package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper;

import com.japs.backend.backend_BodyFitGym.domain.model.Role;
import com.japs.backend.backend_BodyFitGym.domain.model.User;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    void toEntity_devuelveNullConEntradaNull() {
        assertThat(UserMapper.toEntity(null)).isNull();
    }

    @Test
    void toModel_devuelveNullConEntradaNull() {
        assertThat(UserMapper.toModel(null)).isNull();
    }

    @Test
    void mapeoIdaYVuelta_conservaTodosLosCampos() {
        User original = User.builder()
                .id(1L)
                .document("1234567890")
                .name("Administrador")
                .lastName("BodyFitGym")
                .userName("admin")
                .password("hashed-password")
                .status("Activo")
                .role(Role.ADMINISTRADOR)
                .registrationDate(LocalDate.of(2026, 1, 1))
                .build();

        User result = UserMapper.toModel(UserMapper.toEntity(original));

        assertThat(result).usingRecursiveComparison().isEqualTo(original);
    }

    @Test
    void toEntity_mapeaLosCamposClave() {
        User user = User.builder()
                .id(7L)
                .userName("recep")
                .role(Role.RECEPCIONISTA)
                .build();

        UserEntity entity = UserMapper.toEntity(user);

        assertThat(entity.getId()).isEqualTo(7L);
        assertThat(entity.getUserName()).isEqualTo("recep");
        assertThat(entity.getRole()).isEqualTo(Role.RECEPCIONISTA);
    }
}
