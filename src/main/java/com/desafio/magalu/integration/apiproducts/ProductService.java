package com.desafio.magalu.integration.apiproducts;

import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.exception.ObjectNotFoundException;
import com.desafio.magalu.exception.ProductApiException;
import com.desafio.magalu.mapper.ProductMapper;
import com.desafio.magalu.repository.product.ProductEntity;
import com.desafio.magalu.repository.product.ProductRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductClient productClient;

    public ProductDomain consultProduct(ProductDomain productDomain){
        String idProduct = productDomain.getIdProduct();

        ProductReponse productReponse = null;

        Optional<ProductEntity> optProd = productRepository.findByIdProduct(productDomain.getIdProduct());

        if(optProd.isPresent()){
            return Mappers.getMapper(ProductMapper.class).entityToDomain(optProd.get());
        }

        try{
            productReponse = productClient.detailProduct(idProduct).getBody();
            productDomain = Mappers.getMapper(ProductMapper.class).responseToDomain(productReponse);
        }catch (Exception e){
            throw new ProductApiException(e.getMessage());
        }
        return productDomain;
    }

    public ProductDomain read(String idProduct){

        Optional<ProductEntity> optPrd = productRepository.findByIdProduct(idProduct);

        if(optPrd.isPresent()){
            return Mappers.getMapper(ProductMapper.class).entityToDomain(optPrd.get());
        }
        throw new ObjectNotFoundException("Product Not Found.");
    }


}
