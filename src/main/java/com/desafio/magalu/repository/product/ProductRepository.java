package com.desafio.magalu.repository.product;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Cacheable(cacheNames = "ProductByIdBD", key = "#idProduct")
    Optional<ProductEntity> findByIdProduct(String idProduct);

}