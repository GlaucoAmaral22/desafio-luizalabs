package com.desafio.magalu.integration.apiproducts;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductReponse {

    @JsonProperty("price")
    public Double price;

    @JsonProperty("image")
    public String image;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("id")
    public String idProduct;

    @JsonProperty("title")
    public String title;

}
