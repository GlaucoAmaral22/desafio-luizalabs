package com.desafio.magalu.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProductResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("title")
    private String title;

    @JsonProperty("image")
    private String image;

}
