package com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.security.adapter;

import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.entities.UserEntity;
import com.japs.backend.backend_BodyFitGym.infrastructure.driven_adapters.postgres.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MyUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity user = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return UserPrincipal.create(user);
    }
}
