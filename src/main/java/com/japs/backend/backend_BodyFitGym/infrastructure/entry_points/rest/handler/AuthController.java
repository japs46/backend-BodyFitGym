package com.japs.backend.backend_BodyFitGym.infrastructure.entry_points.rest.handler;

import com.japs.backend.backend_BodyFitGym.application.dto.LoginRequest;
import com.japs.backend.backend_BodyFitGym.application.response.ApiResponse;
import com.japs.backend.backend_BodyFitGym.application.response.AuthResponse;
import com.japs.backend.backend_BodyFitGym.application.utils.ResponseBuilder;
import com.japs.backend.backend_BodyFitGym.domain.port.in.auth.IAuthUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthUseCase authUseCase;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {

        log.info("[AuthController] POST /login - Usuario: {}", loginRequest.getUserName());

        AuthResponse authResponse = authUseCase.authenticate(loginRequest.getUserName(), loginRequest.getPassword());
        ApiResponse<AuthResponse> apiResponse = ResponseBuilder.successMessage("Sesión iniciada", authResponse);

        log.info("[AuthController] POST /login - Sesión iniciada para: {}", loginRequest.getUserName());
        return ResponseEntity.ok(apiResponse);
    }
}
