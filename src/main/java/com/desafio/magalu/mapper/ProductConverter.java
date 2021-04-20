package com.desafio.magalu.mapper;



import com.desafio.magalu.controller.request.ProductRequest;
import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.service.apiproducts.ProductReponseApi;
import org.mapstruct.Mapper;

@Mapper
public interface ProductConverter {

    ProductDomain requestToDomain(ProductRequest productRequest);

    ProductDomain responseToDomain(ProductReponseApi productReponseApi);


}
