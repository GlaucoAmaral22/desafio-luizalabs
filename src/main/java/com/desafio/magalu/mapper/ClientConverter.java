package com.desafio.magalu.mapper;


import com.desafio.magalu.controller.request.ClientRequest;
import com.desafio.magalu.controller.response.ClientResponse;
import com.desafio.magalu.domain.ClientDomain;
import com.desafio.magalu.repository.client.ClientEntity;
import org.mapstruct.Mapper;

@Mapper
public interface ClientConverter {

    public ClientDomain requestToDomain(ClientRequest clientRequest);

    public ClientEntity domainToEntity(ClientDomain clientDomain);

    public ClientResponse domainToResponse(ClientDomain clientDomain);

    public ClientDomain entityToDomain(ClientEntity clientEntity);

}
