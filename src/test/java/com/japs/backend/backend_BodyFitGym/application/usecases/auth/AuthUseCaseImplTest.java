package com.japs.backend.backend_BodyFitGym.application.usecases.auth;

import com.japs.backend.backend_BodyFitGym.application.response.AuthResponse;
import com.japs.backend.backend_BodyFitGym.domain.model.Role;
import com.japs.backend.backend_BodyFitGym.domain.model.User;
import com.japs.backend.backend_BodyFitGym.domain.port.out.JwtTokenProviderPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.UserRepositoryPort;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.UserEntity;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.security.adapter.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock
    private JwtTokenProviderPort jwtTokenProviderPort;

    @InjectMocks
    private AuthUseCaseImpl authUseCase;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private UserEntity userEntity() {
        return UserEntity.builder()
                .id(1L)
                .userName("admin")
                .name("Administrador")
                .lastName("BodyFitGym")
                .document("0000000000")
                .role(Role.ADMINISTRADOR)
                .build();
    }

    @Test
    void authenticate_retornaTokenYDatosDelUsuarioAutenticado() {
        UserPrincipal principal = UserPrincipal.create(userEntity());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "password");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProviderPort.generateToken(authentication)).thenReturn("fake-jwt-token");

        AuthResponse response = authUseCase.authenticate("admin", "password");

        assertThat(response.getToken()).isEqualTo("fake-jwt-token");
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUserName()).isEqualTo("admin");
        assertThat(response.getName()).isEqualTo("Administrador");
        assertThat(response.getLastName()).isEqualTo("BodyFitGym");
        assertThat(response.getDocument()).isEqualTo("0000000000");
        assertThat(response.getRole()).isEqualTo(Role.ADMINISTRADOR);
        assertThat(response.getExpiresIn()).isEqualTo(86400);
    }

    @Test
    void authenticate_dejaLaAutenticacionEnElContextoDeSeguridad() {
        UserPrincipal principal = UserPrincipal.create(userEntity());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "password");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProviderPort.generateToken(authentication)).thenReturn("fake-jwt-token");

        authUseCase.authenticate("admin", "password");

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(authentication);
    }

    @Test
    void getAuthenticatedUser_retornaElUsuarioDelUserNameAutenticado() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = User.builder().id(1L).userName("admin").build();
        when(userRepositoryPort.findByUserName("admin")).thenReturn(Optional.of(user));

        User result = authUseCase.getAuthenticatedUser();

        assertThat(result).isSameAs(user);
    }

    @Test
    void getAuthenticatedUser_lanzaCuandoElUsuarioNoExiste() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("fantasma", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepositoryPort.findByUserName("fantasma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authUseCase.getAuthenticatedUser())
                .isInstanceOf(IllegalStateException.class);
    }
}
