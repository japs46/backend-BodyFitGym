package com.japs.backend.backend_BodyFitGym.application.usecases.user;

import com.japs.backend.backend_BodyFitGym.domain.model.User;
import com.japs.backend.backend_BodyFitGym.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private DeleteUserUseCaseImpl deleteUserUseCase;

    @Test
    void deleteUser_eliminaCuandoExiste() {
        when(userRepositoryPort.findById(1L))
                .thenReturn(Optional.of(User.builder().id(1L).build()));

        deleteUserUseCase.deleteUser(1L);

        verify(userRepositoryPort).delete(1L);
    }

    @Test
    void deleteUser_lanzaCuandoNoExiste() {
        when(userRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteUserUseCase.deleteUser(99L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");

        verify(userRepositoryPort, never()).delete(99L);
    }
}
