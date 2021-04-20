package com.desafio.magalu.controller.response;


import com.desafio.magalu.domain.ProductDomain;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CustomPaginationReponse {

    @JsonProperty("content")
    List<ProductDomain> favoriteProducts;




}
