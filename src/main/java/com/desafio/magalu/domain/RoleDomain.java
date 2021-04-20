package com.desafio.magalu.domain;

import java.io.Serializable;

public class RoleDomain implements Serializable {

    private Long id;

    private String name;

    public RoleDomain() {
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
}
