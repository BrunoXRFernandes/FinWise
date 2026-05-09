package com.finwise.infrastructure.persistence;

import com.finwise.domain.model.User;
import com.finwise.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JpaUserRepositoryAdapter implements UserRepositoryPort {
    private final SpringDataUserRepository userRepository;
    private final AccountPersistenceMapper mapper;

    public JpaUserRepositoryAdapter(SpringDataUserRepository userRepository, AccountPersistenceMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsById(UUID userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserJpaEntity saved = userRepository.save(mapper.toEntity(user));
        return mapper.toDomain(saved);
    }
}

