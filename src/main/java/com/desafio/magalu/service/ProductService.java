package com.desafio.magalu.service;

import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.exception.ObjectNotFoundException;
import com.desafio.magalu.exception.ProductApiException;
import com.desafio.magalu.mapper.ProductConverter;
import com.desafio.magalu.repository.product.ProductEntity;
import com.desafio.magalu.repository.product.ProductRepository;
import com.desafio.magalu.service.apiproducts.ProductClient;
import com.desafio.magalu.service.apiproducts.ProductReponse;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    public ProductService(ProductRepository productRepository, ProductClient productClient) {
        this.productRepository = productRepository;
        this.productClient = productClient;
    }

    ProductRepository productRepository;

    ProductClient productClient;

    public ProductDomain consultProduct(ProductDomain productDomain){
        String idProduct = productDomain.getIdProduct();

        ProductReponse productReponse = null;

        Optional<ProductEntity> optProd = productRepository.findByIdProduct(productDomain.getIdProduct());

        if(optProd.isPresent()){
            return Mappers.getMapper(ProductConverter.class).entityToDomain(optProd.get());
        }

        try{
            productReponse = productClient.detailProduct(idProduct).getBody();
            productDomain = Mappers.getMapper(ProductConverter.class).responseToDomain(productReponse);
        }catch (Exception e){
            throw new ProductApiException("Error contacting products api.");
        }
        return productDomain;
    }

    public ProductDomain read(String idProduct){

        Optional<ProductEntity> optPrd = productRepository.findByIdProduct(idProduct);

        if(optPrd.isPresent()){
            return Mappers.getMapper(ProductConverter.class).entityToDomain(optPrd.get());
        }
        throw new ObjectNotFoundException("Product Not Found.");
    }


}
