package com.japs.backend.backend_BodyFitGym.domain.port.out;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenProviderPort {

    String generateToken(Authentication authentication);

    boolean validateToken(String token, UserDetails userDetails);

    String getUserNameFromToken(String token);
}
