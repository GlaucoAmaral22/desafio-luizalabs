package com.desafio.magalu.mapper;


import com.desafio.magalu.controller.request.UserRequest;
import com.desafio.magalu.controller.response.UserResponse;
import com.desafio.magalu.domain.UserDomain;
import com.desafio.magalu.repository.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserConverter {

    public UserDomain requestToDomain(UserRequest clientRequest);

    public UserEntity domainToEntity(UserDomain clientDomain);

    public UserResponse domainToResponse(UserDomain clientDomain);

    public UserDomain entityToDomain(UserEntity clientEntity);

}
