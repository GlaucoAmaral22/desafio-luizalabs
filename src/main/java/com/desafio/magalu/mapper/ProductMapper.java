package com.desafio.magalu.mapper;



import com.desafio.magalu.controller.request.ProductRequest;
import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.integration.apiproducts.ProductReponse;
import com.desafio.magalu.repository.product.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductMapper {

    ProductDomain requestToDomain(ProductRequest productRequest);

    ProductDomain responseToDomain(ProductReponse productReponse);

    ProductDomain entityToDomain(ProductEntity productEntity);

    ProductEntity domainToEntity(ProductDomain productDomain);


}
