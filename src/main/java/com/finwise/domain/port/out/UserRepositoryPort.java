package com.finwise.domain.port.out;

import com.finwise.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    boolean existsById(UUID userId);

    Optional<User> findByEmail(String email);

    User save(User user);
}

