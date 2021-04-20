package com.desafio.magalu.mapper;


import com.desafio.magalu.domain.RoleDomain;
import com.desafio.magalu.repository.user.RoleEntity;
import org.mapstruct.Mapper;

@Mapper
public interface RoleConverter {

    RoleDomain entityToDomain(RoleEntity roleEntity);

}
