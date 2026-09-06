package com.japs.backend.backend_BodyFitGym.application.usecases.auth;

import com.japs.backend.backend_BodyFitGym.application.response.AuthResponse;
import com.japs.backend.backend_BodyFitGym.domain.model.User;
import com.japs.backend.backend_BodyFitGym.domain.port.in.auth.IAuthUseCase;
import com.japs.backend.backend_BodyFitGym.domain.port.out.JwtTokenProviderPort;
import com.japs.backend.backend_BodyFitGym.domain.port.out.UserRepositoryPort;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.mapper.UserMapper;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.security.adapter.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthUseCaseImpl implements IAuthUseCase {

    private final AuthenticationManager authenticationManager;
    private final UserRepositoryPort userRepositoryPort;
    private final JwtTokenProviderPort jwtTokenProviderPort;

    @Override
    public AuthResponse authenticate(String userName, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProviderPort.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = UserMapper.toModel(userPrincipal.getUser());

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .userName(user.getUserName())
                .name(user.getName())
                .lastName(user.getLastName())
                .document(user.getDocument())
                .role(user.getRole())
                .expiresIn(86400)
                .build();
    }

    @Override
    public User getAuthenticatedUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepositoryPort.findByUserName(userName)
                .orElseThrow(() -> new IllegalStateException("Usuario no autenticado"));
    }
}
