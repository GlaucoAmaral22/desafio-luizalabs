package com.desafio.magalu.service;

import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.exception.ProductApiException;
import com.desafio.magalu.mapper.ProductConverter;
import com.desafio.magalu.service.apiproducts.ProductClient;
import com.desafio.magalu.service.apiproducts.ProductReponseApi;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    public ProductService(ProductClient productClient) {
        this.productClient = productClient;
    }

    ProductClient productClient;

    @Cacheable(cacheNames = "ProductById", key = "#idProduct")
    public ProductDomain read(String idProduct){

        ProductDomain productDomain = null;
        ProductReponseApi productReponseApi = null;

        try{
            productReponseApi = productClient.detailProduct(idProduct).getBody();
            productDomain = Mappers.getMapper(ProductConverter.class).responseToDomain(productReponseApi);
        }catch (Exception e){
            throw new ProductApiException("Error contacting products api.");
        }
        return productDomain;

    }


}
