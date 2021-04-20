package com.desafio.magalu.service.apiproducts;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "productClient", url = "${product.api}")
public interface ProductClient {

    @GetMapping("{idProduct}/")
    public ResponseEntity<ProductReponseApi> detailProduct(@PathVariable("idProduct") String idProduct);

}
