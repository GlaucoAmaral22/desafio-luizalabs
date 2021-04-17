package com.desafio.magalu.domain;

import java.util.Set;

public class ClientDomain {

    private Long id;

    private String name;

    private String email;

    private Set<ProductDomain> favoriteProducts;

    public ClientDomain() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<ProductDomain> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void setFavoriteProducts(Set<ProductDomain> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }
}
