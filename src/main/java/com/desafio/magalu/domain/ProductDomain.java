package com.desafio.magalu.domain;


import java.io.Serializable;

public class ProductDomain implements Serializable, Comparable {

    private Long id;

    private String idProduct;

    public ProductDomain() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public int compareTo(Object o) {
        ProductDomain prod = (ProductDomain) o;
        return getId().compareTo(prod.getId());
    }
}
