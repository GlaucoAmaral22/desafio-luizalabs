package com.desafio.magalu.domain;


import com.desafio.magalu.repository.user.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


public class UserDomain implements Serializable {

    private Long id;
    private String name;
    private String email;
    private List<RoleDomain> roles;
    private Set<ProductDomain> favoriteProducts;

    private String token;

    public static UserDomain create(UserEntity user, String token) {
        UserDomain userDomain = new UserDomain();
        userDomain.setName(user.getName());
        userDomain.setEmail(user.getEmail());
        userDomain.token = token;
        return userDomain;
    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public UserDomain() {
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<ProductDomain> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void setFavoriteProducts(Set<ProductDomain> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }

    public List<RoleDomain> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDomain> roles) {
        this.roles = roles;
    }
}
