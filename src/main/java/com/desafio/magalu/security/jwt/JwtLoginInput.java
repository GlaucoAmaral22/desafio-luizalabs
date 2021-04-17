package com.desafio.magalu.security.jwt;

class JwtLoginInput {
    private String name;
    private String email;

    public JwtLoginInput() {
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
}