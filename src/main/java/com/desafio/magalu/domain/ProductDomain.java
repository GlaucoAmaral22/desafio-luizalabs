package com.desafio.magalu.domain;


import java.io.Serializable;
import java.util.Objects;

public class ProductDomain implements Serializable, Comparable {


    private String id;

    private Double price;

    private String title;

    private String image;

    public ProductDomain() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public int compareTo(Object o) {
        ProductDomain prod = (ProductDomain) o;
        return getId().compareTo(prod.getId());
    }
}
