package com.desafio.magalu.domain;


import com.desafio.magalu.repository.user.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class UserDomain {

    private String login;
    private String senha;

    // token jwt
    private String token;

    public static UserDomain create(UserEntity user, String token) {
        UserDomain userDomain = new UserDomain();
        userDomain.setLogin(user.getLogin());
        userDomain.setSenha(user.getSenha());
        userDomain.token = token;
        return userDomain;
    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public UserDomain() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
