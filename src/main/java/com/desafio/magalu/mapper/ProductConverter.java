package com.desafio.magalu.mapper;



import com.desafio.magalu.controller.request.ProductRequest;
import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.service.apiproducts.ProductReponseApi;
import com.desafio.magalu.repository.product.ProductEntity;
import org.mapstruct.Mapper;

@Mapper
public interface ProductConverter {

    ProductDomain requestToDomain(ProductRequest productRequest);

    ProductDomain responseToDomain(ProductReponseApi productReponseApi);

    ProductDomain entityToDomain(ProductEntity productEntity);

    ProductEntity domainToEntity(ProductDomain productDomain);

}
