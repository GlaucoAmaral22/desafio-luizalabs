package com.desafio.magalu.service;

import com.desafio.magalu.domain.RoleDomain;
import com.desafio.magalu.mapper.RoleConverter;
import com.desafio.magalu.mapper.UserConverter;
import com.desafio.magalu.repository.user.RoleEntity;
import com.desafio.magalu.repository.user.RoleRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {


    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    RoleRepository roleRepository;

    RoleConverter roleConverter  = Mappers.getMapper(RoleConverter.class); ;

    @Cacheable(cacheNames = "RoleName", key = "#nameRole")
    public RoleDomain read(String nameRole) {

        Optional<RoleEntity> optRole = roleRepository.findByName(nameRole);

        RoleEntity roleEntity = null;

        if (!optRole.isPresent()) {
            RoleEntity newRoleUser = new RoleEntity();
            newRoleUser.setName("ROLE_USER");
            roleEntity = roleRepository.save(newRoleUser);
        } else {
            roleEntity = optRole.get();
        }

        return roleConverter.entityToDomain(roleEntity);

    }


}
