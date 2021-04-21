package com.desafio.magalu.service;

import com.desafio.magalu.controller.response.ProductResponse;
import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.exception.ProductApiException;
import com.desafio.magalu.mapper.ProductConverter;
import com.desafio.magalu.repository.product.ProductEntity;
import com.desafio.magalu.repository.product.ProductRepository;
import com.desafio.magalu.service.apiproducts.ProductClient;
import com.desafio.magalu.service.apiproducts.ProductReponseApi;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    public ProductService(ProductClient productClient, ProductRepository productRepository) {
        this.productClient = productClient;
        this.productRepository = productRepository;
    }

    private ProductClient productClient;

    private ProductRepository productRepository;

    private ProductConverter productConverter = Mappers.getMapper(ProductConverter.class);

    @Cacheable(cacheNames = "ProductById", key = "#idProduct")
    public ProductDomain read(String idProduct) {

        ProductDomain productDomain = null;
        ProductEntity productEntity = null;
        ProductReponseApi productReponseApi = null;
        Optional<ProductEntity> optProd = productRepository.findByIdProduct(idProduct);

        if(optProd.isPresent()){
            return productConverter.entityToDomain(optProd.get());
        }

        try {
            productReponseApi = productClient.detailProduct(idProduct).getBody();
            productEntity = productConverter.responseToEntity(productReponseApi);
            productRepository.save(productEntity);
            productDomain = productConverter.entityToDomain(productEntity);
        } catch (Exception e) {
            throw new ProductApiException("Error contacting products api.");
        }
        return productDomain;
    }


    public Set<ProductResponse> converterToResponsePageModel(Set<ProductEntity> productEntities) {
        return this.productConverter.setEntityToResponse(productEntities);
    }







}
