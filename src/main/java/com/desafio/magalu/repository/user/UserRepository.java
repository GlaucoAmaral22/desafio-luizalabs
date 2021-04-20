package com.desafio.magalu.repository.user;

import com.desafio.magalu.repository.product.ProductEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Cacheable(cacheNames = "UserByEmail", key = "#email")
    Optional<UserEntity> findByEmail(String email);
}
