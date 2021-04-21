package com.desafio.magalu.mapper;



import com.desafio.magalu.controller.request.ProductRequest;
import com.desafio.magalu.controller.response.ProductResponse;
import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.service.apiproducts.ProductReponseApi;
import com.desafio.magalu.repository.product.ProductEntity;
import org.mapstruct.Mapper;
import java.util.Set;

@Mapper
public interface ProductConverter {

    ProductDomain requestToDomain(ProductRequest productRequest);

    ProductEntity responseToEntity(ProductReponseApi productReponseApi);

    ProductDomain entityToDomain(ProductEntity productEntity);

    ProductEntity domainToEntity(ProductDomain productDomain);

    Set<ProductResponse> setEntityToResponse(Set<ProductEntity> productEntities);

}