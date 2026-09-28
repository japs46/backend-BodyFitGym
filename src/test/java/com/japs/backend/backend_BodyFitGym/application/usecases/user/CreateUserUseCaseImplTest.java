package com.japs.backend.backend_BodyFitGym.application.usecases.user;

import com.japs.backend.backend_BodyFitGym.domain.model.Role;
import com.japs.backend.backend_BodyFitGym.domain.model.User;
import com.japs.backend.backend_BodyFitGym.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    private final BCryptPasswordEncoder realEncoder = new BCryptPasswordEncoder();

    // No se usa @InjectMocks: el encoder debe ser una instancia real (no un
    // mock) para poder verificar que la contraseña queda efectivamente
    // encriptada, no solo que se llamó al método.
    private CreateUserUseCaseImpl createUserUseCase;

    @BeforeEach
    void setUp() {
        createUserUseCase = new CreateUserUseCaseImpl(userRepositoryPort, realEncoder);
    }

    private User newUser() {
        return User.builder()
                .document("1234567890")
                .name("Reception")
                .lastName("Test")
                .userName("recep")
                .password("Test123$")
                .role(Role.RECEPCIONISTA)
                .build();
    }

    @Test
    void createUser_lanzaCuandoElUserNameYaExiste() {
        User input = newUser();
        when(userRepositoryPort.findByUserName("recep"))
                .thenReturn(Optional.of(User.builder().id(1L).userName("recep").build()));

        assertThatThrownBy(() -> createUserUseCase.createUser(input))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("recep");

        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    void createUser_lanzaCuandoElDocumentoYaExiste() {
        User input = newUser();
        when(userRepositoryPort.findByUserName("recep")).thenReturn(Optional.empty());
        when(userRepositoryPort.findByDocument("1234567890"))
                .thenReturn(Optional.of(User.builder().id(1L).document("1234567890").build()));

        assertThatThrownBy(() -> createUserUseCase.createUser(input))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("1234567890");

        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    void createUser_encriptaLaContraseniaAntesDeGuardar() {
        User input = newUser();
        when(userRepositoryPort.findByUserName("recep")).thenReturn(Optional.empty());
        when(userRepositoryPort.findByDocument("1234567890")).thenReturn(Optional.empty());
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = createUserUseCase.createUser(input);

        assertThat(result.getPassword()).isNotEqualTo("Test123$");
        assertThat(realEncoder.matches("Test123$", result.getPassword())).isTrue();
    }
}
