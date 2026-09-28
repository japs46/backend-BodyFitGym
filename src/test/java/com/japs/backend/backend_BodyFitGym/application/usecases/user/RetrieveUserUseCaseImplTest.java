package com.japs.backend.backend_BodyFitGym.application.usecases.user;

import com.japs.backend.backend_BodyFitGym.application.dto.UserSearchCriteria;
import com.japs.backend.backend_BodyFitGym.domain.model.User;
import com.japs.backend.backend_BodyFitGym.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveUserUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private RetrieveUserUseCaseImpl retrieveUserUseCase;

    @Test
    void getUserById_lanzaCuandoIdEsNull() {
        assertThatThrownBy(() -> retrieveUserUseCase.getUserById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getUserById_lanzaCuandoIdNoEsPositivo() {
        assertThatThrownBy(() -> retrieveUserUseCase.getUserById(0L))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> retrieveUserUseCase.getUserById(-5L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getUserById_lanzaNoSuchElementCuandoNoExiste() {
        when(userRepositoryPort.findById(50L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> retrieveUserUseCase.getUserById(50L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("50");
    }

    @Test
    void getUserById_retornaCuandoExiste() {
        User user = User.builder().id(1L).userName("admin").build();
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));

        User result = retrieveUserUseCase.getUserById(1L);

        assertThat(result).isSameAs(user);
    }

    @Test
    void getUserByDocument_lanzaCuandoEsNuloOVacio() {
        assertThatThrownBy(() -> retrieveUserUseCase.getUserByDocument(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> retrieveUserUseCase.getUserByDocument(" "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getUserByDocument_lanzaCuandoContieneCaracteresNoNumericos() {
        assertThatThrownBy(() -> retrieveUserUseCase.getUserByDocument("12A456"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getUserByDocument_lanzaNoSuchElementCuandoNoExiste() {
        when(userRepositoryPort.findByDocument("1234567890")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> retrieveUserUseCase.getUserByDocument("1234567890"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("1234567890");
    }

    @Test
    void getUserByDocument_retornaCuandoExiste() {
        User user = User.builder().id(1L).document("1234567890").build();
        when(userRepositoryPort.findByDocument("1234567890")).thenReturn(Optional.of(user));

        User result = retrieveUserUseCase.getUserByDocument("1234567890");

        assertThat(result).isSameAs(user);
    }

    @Test
    void getUserByUserName_lanzaCuandoEsNuloOVacio() {
        assertThatThrownBy(() -> retrieveUserUseCase.getUserByUserName(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> retrieveUserUseCase.getUserByUserName(" "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getUserByUserName_lanzaNoSuchElementCuandoNoExiste() {
        when(userRepositoryPort.findByUserName("admin")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> retrieveUserUseCase.getUserByUserName("admin"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("admin");
    }

    @Test
    void getUserByUserName_retornaCuandoExiste() {
        User user = User.builder().id(1L).userName("admin").build();
        when(userRepositoryPort.findByUserName("admin")).thenReturn(Optional.of(user));

        User result = retrieveUserUseCase.getUserByUserName("admin");

        assertThat(result).isSameAs(user);
    }

    @Test
    void search_delegaAlPuerto() {
        UserSearchCriteria criteria = new UserSearchCriteria("admin", null, null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(User.builder().id(1L).build()));
        when(userRepositoryPort.search(criteria, pageable)).thenReturn(page);

        Page<User> result = retrieveUserUseCase.search(criteria, pageable);

        assertThat(result).isSameAs(page);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
